// Convert a decimal integer do a binary string
// added a test printf() you can remove later
// Turbo C modified for Pelles C     vegaseat    19nov2004

#include <stdio.h>

void dec2bin(long decimal, char *binary, int numOnes);

int main()
{
  long low, high;
  char binary[80];

  printf("Enter an lower value : ");
  scanf("%ld",&low);
  
  printf("\nEnter an higher value : ");
  scanf("%ld",&high);
  
  for (long i = low; i < high; i++) {
	int numOnes = 0;
	dec2bin(i,binary, numOnes);
  	printf("The binary value of %ld is %s - %d \n",i,binary,&numOnes);
  }

  return 0;
}

//
// accepts a decimal integer and returns a binary coded string
//
void dec2bin(long decimal, char *binary, int *numOnes)
{
  int  k = 0, n = 0;
  int  neg_flag = 0;
  int  remain;
  char temp[80];

  // take care of negative input
  if (decimal < 0)
  {      
    decimal = -decimal;
    neg_flag = 1;
  }
  do 
  {
    remain    = decimal % 2;
	if(remain == 1) numOnes++;
    // whittle down the decimal number
    decimal   = decimal / 2;
    // converts digit 0 or 1 to character '0' or '1'
    temp[k++] = remain + '0';
  } while (decimal > 0);

  if (neg_flag)
    temp[k++] = '-';       // add - sign
  else
    temp[k++] = ' ';       // space

  // reverse the spelling
  while (k >= 0)
    binary[n++] = temp[--k];

  binary[n-1] = 0;         // end with NULL
}
