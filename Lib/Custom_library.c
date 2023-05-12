#include "Eziot.h"

Time klokken;

struct timeval tv;

int time_compare(Time t1, Time t2) {
    if (t1.hour > t2.hour) {
        return 1;
    } else if (t1.hour == t2.hour && t1.minute > t2.minute) {
        return 1;
    } else if (t1.hour == t2.hour && t1.minute == t2.minute) {
        return 0;
    }
    return -1;
}

Time make_time(int hour, int minute) {
    Time res;
    res.hour = hour;
    res.minute = minute;

    return res;
}
char* concat(char* str1, char* str2) {
    size_t len = strlen(str1) + strlen(str2) + 1;
    char* res = malloc(len);
    strcpy(res, str1);
    strcat(res, str2);

    return res;
}

int customStrLen(char* str1, int len2) {
    return strlen(str1) + len2;
}

Time time_generator() {
    Time res;
    struct tm* local;
    time_t t = time(NULL);

    // Get the localtime
    local = localtime(&t);

    // Stringify current time
    char* time_str = malloc(sizeof(char) * 12);
    strftime(time_str, 12, "%H", local);
    res.hour = atoi(time_str);

    strftime(time_str, 12, "%M", local);
    res.minute = atoi(time_str);

    free(time_str);
    return res;
}

void update_klokken() {
    klokken = time_generator();
}

bool true_cond() {
    return true;
}

int send_field_to_endpoint(char *endpoint, char *field, void *value_ptr, enum Datatype datatype) {
    char *json;
    cJSON *root;

    // Create JSON from field and value
    root = cJSON_CreateObject();
    switch(datatype) {
    case TYPE_INTEGER:
        cJSON_AddItemToObject(root, field, cJSON_CreateNumber((double) *((int *) value_ptr)));
        break;
    case TYPE_DOUBLE:
        cJSON_AddItemToObject(root, field, cJSON_CreateNumber(*((double *) value_ptr)));
        break;
    case TYPE_STRING:
        cJSON_AddItemToObject(root, field, cJSON_CreateString((char *) value_ptr));
        break;
    case TYPE_BOOL:
        cJSON_AddItemToObject(root, field, cJSON_CreateBool(*((bool *) value_ptr)));
        break;
    }
    json = cJSON_Print(root);

    // Send to endpoint
    CURL *curl;
    CURLcode res;

    char *protocol = "http://";
    char *url = calloc(strlen(protocol)+strlen(endpoint)+1, sizeof(char));
    strcpy(url, protocol);
    strcat(url, endpoint);

    curl = curl_easy_init();
    if(curl) {
        struct curl_slist *hs=NULL;
        hs = curl_slist_append(hs, "Content-Type: application/json");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, hs);
        curl_easy_setopt(curl, CURLOPT_URL, url);
        // curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
        curl_easy_setopt(curl, CURLOPT_POST, 1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, json);

        res = curl_easy_perform(curl);
        /* Check for errors */
        if(res != CURLE_OK)
            fprintf(stderr, "curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
    }

    // Cleanup
    curl_easy_cleanup(curl);
    free(json);
    cJSON_Delete(root);
    free(url);

    return 0;
}

struct string {
    char *ptr;
    size_t len;
};

void init_string(struct string *s) {
    s->len = 0;
    s->ptr = malloc(s->len+1);
    if (s->ptr == NULL) {
        fprintf(stderr, "malloc() failed\n");
        exit(EXIT_FAILURE);
    }
    s->ptr[0] = '\0';
}
    
size_t writefunc(void *ptr, size_t size, size_t nmemb, struct string *s)
{
    size_t new_len = s->len + size*nmemb;
    s->ptr = realloc(s->ptr, new_len+1);
    if (s->ptr == NULL) {
        fprintf(stderr, "realloc() failed\n");
        exit(EXIT_FAILURE);
    }
    memcpy(s->ptr+s->len, ptr, size*nmemb);
    s->ptr[new_len] = '\0';
    s->len = new_len;
    
    return size*nmemb;
}
    
int get_field_from_endpoint(char *endpoint, char *field, void *value_ptr, enum Datatype datatype)
{
    // Send to endpoint
    CURL *curl;
    CURLcode res;
    struct string response;
    init_string(&response);
    
    char *protocol = "http://";
    char *url = calloc(strlen(protocol) + strlen(endpoint) + 1, sizeof(char));
    strcpy(url, protocol);
    strcat(url, endpoint);
    
    curl = curl_easy_init();
    if (curl)
    {
        struct curl_slist *hs = NULL;
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, writefunc);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);
    
        res = curl_easy_perform(curl);
    
        /* Check for errors */
        if (res != CURLE_OK)
            fprintf(stderr, "curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
    }
    
    cJSON *root = cJSON_Parse(response.ptr);
    
    // Read data from JSON result
    switch (datatype) {
        case TYPE_INTEGER:
            *((int *) value_ptr) = cJSON_GetObjectItem(root,field)->valueint;
            break;
        case TYPE_DOUBLE:
            *((double *) value_ptr) = cJSON_GetObjectItem(root,field)->valuedouble;
            break;
        case TYPE_STRING:
            value_ptr = cJSON_GetObjectItem(root,field)->valuestring;
            break;
        case TYPE_BOOL:
            *((bool *) value_ptr) = cJSON_GetObjectItem(root,field)->valueint;
            break;
    }
    
    // Cleanup
    curl_easy_cleanup(curl);
    cJSON_Delete(root);
    free(url);
    free(response.ptr);
    
    return 0;
}


// Event loop

/*
    If statement structs
*/

void init_if_statement(if_statement *statement, void *condition, void *body, unsigned int update_delay) {
    statement->condition = condition;
    statement->body = body;
    statement->last_state = false;
    statement->last_time_checked = tv;
    statement->update_delay = update_delay;
}

/*
    If statement queues
*/

void init_if_queue(if_queue *queue) {
    queue->head = -1;
    queue->tail = -1;
}

bool is_queue_full(if_queue *queue) {
    return (queue->tail+1) % IF_QUEUE_SIZE == queue->head;
}

bool is_queue_empty(if_queue *queue) {
    return queue->head == -1;
}

bool add_to_queue(if_queue *queue, void (*element)()) {
    if(is_queue_full(queue)) return false;
    if(is_queue_empty(queue)) queue->head = 0;
    queue->tail = (queue->tail+1)%IF_QUEUE_SIZE;
    queue->if_bodies[queue->tail] = element;
    return true;
}

bool update_if_check(if_statement *statement, if_queue *task_queue) {
    if(is_queue_full(task_queue)) return false;
    gettimeofday(&tv, NULL);
    if(tv.tv_sec < statement->last_time_checked.tv_sec + statement->update_delay / 1000 ||
            tv.tv_sec <= statement->last_time_checked.tv_sec + statement->update_delay / 1000
            && tv.tv_usec < statement->last_time_checked.tv_usec + (statement->update_delay%1000) * 1000) return false;
    if(statement->condition()) {
        if(!statement->last_state) {
            statement->last_state = true;
            add_to_queue(task_queue, statement->body);
        }
    } else statement->last_state = false;
    statement->last_time_checked = tv;
    return true;
}

bool remove_from_queue(if_queue *queue) {
    if(is_queue_empty(queue)) return false;
    if(queue->head == queue->tail) init_if_queue(queue);
    else queue->head = (queue->head+1)%IF_QUEUE_SIZE;
    return true;
}

void (*get_from_queue(if_queue *queue))() {
    return queue->if_bodies[queue->head];
}

/*
    Multi threading
*/

run_if_thread_args *init_run_if_thread_args(int *thread_count, void (*body)(), pthread_mutex_t *thread_count_lock) {
    run_if_thread_args *args = malloc(sizeof(run_if_thread_args));
    args->body = body;
    args->thread_count = thread_count;
    args->thread_count_lock = thread_count_lock;
    return args;
}

void run_if_thread(void *_args) {
    run_if_thread_args *args = (run_if_thread_args *) _args;
    args->body();
    pthread_mutex_lock(args->thread_count_lock);
    (*(args->thread_count))--;
    pthread_mutex_unlock(args->thread_count_lock);
    free(args);
}
