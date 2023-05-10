#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <string.h>
#include <curl/curl.h>
#include <cjson/cJSON.h>

#include <pthread.h>
#include <windows.h>

typedef struct {
    int hour;
    int minute;
} Time;

int time_compare(Time t1, Time t2);
Time make_time(int hour, int minute);
char* concat(char* str1, char* str2);
int customStrLen(char* str1, int len2);
Time time_generator();

enum Datatype {
    TYPE_INTEGER,
    TYPE_DOUBLE,
    TYPE_STRING,
    TYPE_BOOL
};

int send_field_to_endpoint(char *endpoint, char *field, void *value_ptr, enum Datatype datatype);
int get_field_from_endpoint(char *endpoint, char *field, void *value_ptr, enum Datatype datatype);

// Event loop

#define IF_QUEUE_SIZE 16
#define MAX_THREADS 8

// If statement structs
typedef struct {
    bool (*condition)();
    void (*body)();
    bool last_state;
} if_statement;

void init_if_statement(if_statement *statement, void *condition, void *body);

// If statement queues
typedef struct {
    void (*if_bodies[IF_QUEUE_SIZE])();
    int head;
    int tail;
} if_queue;

void init_if_queue(if_queue *queue);
bool is_queue_full(if_queue *queue);
bool is_queue_empty(if_queue *queue);
bool add_to_queue(if_queue *queue, void (*element)());
bool remove_from_queue(if_queue *queue);
void (*get_from_queue(if_queue *queue))();

// Multi threading
typedef struct {
    int *thread_count;
    void (*body)();
    pthread_mutex_t *thread_count_lock;
} run_if_thread_args;

run_if_thread_args *init_run_if_thread_args(int *thread_count, void (*body)(), pthread_mutex_t *thread_count_lock);
void *run_if_thread(void *_args);
