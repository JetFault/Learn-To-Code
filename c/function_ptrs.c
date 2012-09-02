#include <stdio.h>
#include <stdlib.h>

typedef void (*func)();

void func1() {
	printf("Function 1 called");
}

void func2() {
	printf("Function 2 called");
}

int main(int argc, char** argv) {
	int a = 1;
	func fun;

	if( a == 1 ) fun = &func1;
	else fun = &func2;

	fun();
}

