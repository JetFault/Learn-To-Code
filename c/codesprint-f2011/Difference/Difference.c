#include <stdio.h>
#include <stdlib.h>

int compInts(const void *a, const void *b) {
       const int *ia = (const int *)a;
       const int *ib = (const int *)b;

       if (*ia == *ib) return 0;
       else if (*ia < *ib) return -1;
       else return 1;
}

int main(int argc, char** argv) {
       int N, K, tmp, answer = 0;
       int* numbers;
       scanf("%d", &N);
       scanf("%d", &K);

       numbers = (int*) malloc(sizeof(int) * N);

       for(int i = 0; (i < N) && (scanf("%d", &tmp) != EOF); i++ ) {
               numbers[i] = tmp;
       }

       qsort(numbers, N, sizeof(int), compInts);

       for(int i = 0; i < N; i++) {
               int key = numbers[i] + K;
               if( bsearch((void *)&key, (void *)&numbers[i], N-i, sizeof(int), compInts) ) {
                       answer++;
               }
       }

       free(numbers);
       printf("%d", answer);
       return 0;
}
