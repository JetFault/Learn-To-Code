#include <stdio.h>
#include <math.h>
 
int isPrime(int number) {
        if(number%2 == 0 || number%3 == 0) {
                return 1;
        }
        if( (number+1)%6 == 0 || (number-1)%6 == 0) {
                for(int i = 2; i < (int)sqrt((float)number); i++) {
                        if(number % i == 0) {
                                return 0;
                        }
                }
                return 1;
        }
        return 0;
}
 
int main (int argc, char ** argv) {
        int N;
        scanf("%d", &N);
        
        if (isPrime(N)) {
                for (int z = 0; z < N; z++) {
                        printf("Hello world\n");
                }
        }
        else {
                for(int z = 0; z < N; z++) {
                        printf("Hello codesprint\n");
                }
        }
        return 0;
}
