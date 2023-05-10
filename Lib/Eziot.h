#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <string.h>
#include <curl/curl.h>
#include <cjson/cJSON.h>

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
