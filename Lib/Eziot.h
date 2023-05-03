#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <string.h>

typedef struct {
    int hour;
    int minute;
} Time;

int time_compare(Time t1, Time t2);
Time make_time(int hour, int minute);
char* concat(char* str1, char* str2);
int customStrLen(char* str1, int len2);
Time time_generator();