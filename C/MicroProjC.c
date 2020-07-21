/*******************************************************************
* Project: MicroProjectOne_C
* Author: Marcus Giarrusso
* Date: 09/11/17
* Description: A C program which contains a function called 
*              invert_case which inverts the case of each letter 
*              in the specified string; a main function which asks
*              the user for an input string, calls invert_case using
*              a pointer to that string, and prints the output.
*
********************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char* invert_case(char * input);

int main()
{
    char *inputStr;
    inputStr = malloc(100 * sizeof(char));
    
    printf("Enter a string: ");
    fgets(inputStr, 100, stdin);
    
    printf("Length of string: %lu\n", strlen(inputStr) - 1);
    
    invert_case(inputStr);
    printf("Inverted string: %s\n", inputStr);
    
    return 0;
}

char* invert_case(char * input)
{
    while (*input != '\0')
    {
        if (*input >= 'a' && *input <= 'z')
        {
            *input = *input - 32;
        }
        else if (*input >= 'A' && *input <= 'Z')
        {
            *input = *input + 32;
        }

        input++;
    }
    
    return input;
}

