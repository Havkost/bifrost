#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <string.h>
#include "Eziot.h"

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
