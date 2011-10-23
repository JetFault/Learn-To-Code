CC=gcc
CFLAGS=-Wall -g
#SOURCES=ex1.cpp ex3.cpp
#OBJECTS=ex1.o ex3.o

#all: $(OBJECTS)
	#$(CC) $(inputs) -o $(output)
all: *.o

%.o: %.c
	$(CC) $(CFLAGS) $(input) -o $(output)

#ex1: ex1.c
	#$(CC) $(CFLAGS) ex1.c -o ex1.o

#ex3: ex3.c
	#$(CC) $(CFLAGS) ex3.c -o ex3.o

clean:
	rm -f *.o
