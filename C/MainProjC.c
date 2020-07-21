/*******************************************************************
 * Project: MainProjectOne_C
 * Author: Marcus Giarrusso
 * Date: 09/20/17
 * Description: A C program which produces auto-complete suggestions,
 *              similar to what Bash does when beginning to type a
 *              command and pressing <tab>. It will read all of the 
 *              file names in a user-provided directory and store them
 *              in a data structure made up of an (at least) 26-element
 *              array of linked lists.
 *
 ********************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <dirent.h>
#include <unistd.h>
#include <errno.h>
#include <stdbool.h>
#include <string.h>

void parseDirectory(char[]);
long retrieveId(char);
bool checkequal(char[], char[], long);

typedef struct node
{
    char * x[100];
    struct node * next;
    
} node_t;

/*
 * Main Method
 */
int main(int argc, char** argv)
{
    char directoryName[100];
    
    //Prompts user for directory
    printf( "Enter a directory :");
    scanf("%s", directoryName);
    
    //Passes input to directory parsing
    parseDirectory(directoryName);
    return 0;
}

void parseDirectory(char directoryName[100])
{
    //Initializing linked list
    node_t * list[27];
    
    for(int i = 0; i < 27; i++)
    {
        list[i] = NULL;
        list[i] = malloc(sizeof(node_t));
    }
    
    //Read directory tools
    DIR *dir;
    struct dirent *directory;
    dir = opendir(directoryName);
    char *str[100];
    
    //Parse & sort directory
    if(dir != NULL)
    {
        while ((directory = readdir(dir)) != NULL)
        {
            strcpy(str, directory -> d_name);
            long a = (long)str[0];
            long n = retrieveId(a);
            node_t * current = list[n];
            
            while (current->next != NULL)
            {
                current = current->next;
            }
            
            current->next = malloc(sizeof(node_t));
            strcpy(current->next->x, str);
            current->next->next = NULL;
        }
        
        closedir(dir);
    }
    else
    {
        //If no directory exists, prompt user & terminate program
        printf("Directory Not Found! \n");
        return;
    }
    
    //Searches directory files
    char file[30];
    
    while(!checkequal(file, "0", 1))
    {
        printf("\nSearch Directory Contents (0 to quit): ");
        scanf("%s", file);
        
        long b = retrieveId(file[0]);
        long length = strlen(file);
        node_t* node = list[b];
        
        while (node != NULL)
        {
            //Checks string equality, if equal, directory prints
            if (checkequal(node->x, file, length))
            {
                printf("%s \n", node->x);
            }
            
            node = node->next;
        }
    }
}

//Hashing function
long retrieveId(char a)
{
    a = tolower(a);
    long x;
    
    if (isalpha(a))
    {
        x = (long)a - 97;
    }
    else
    {
        x = 26;
    }
    
    return x;
}

//Compares strings for equality
bool checkequal(char a[], char b[], long length)
{
    bool status = false;
    
    for (int i = 0; i < length; i++)
    {
        if (tolower(a[i]) == tolower(b[i]))
        {
            status = true;
        }
        else
        {
            status = false;
            break;
        }
    }
    return status;
}
