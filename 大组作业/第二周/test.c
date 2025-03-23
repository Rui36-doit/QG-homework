#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<string.h>
#include<math.h>
#include <stdlib.h>

typedef struct Node {
	char str[10];
	struct Node* next;
	struct Node* before;
}Node;

typedef struct Stack {
	Node* top;
	Node* button;
}Stack;

//初始化栈
void start(Stack *pstack) {
	pstack->top = NULL;
	pstack->button = NULL;
}

//入栈
void push(Stack *pstack, char *str) {
	Node* p = (Node*)malloc(sizeof(Node));
	p->before = NULL;
	p->next = NULL;
	strcpy(p->str, str);
	if (pstack -> button == NULL) {
		pstack->button = p;
		pstack->top = p;
	}else {
		pstack->top->next = p;
		p->before = pstack->top;
		pstack->top = p;
	}
}

//出栈

char* pop(char* str, Stack *pstack) {
	Node *p = pstack->top->before;
	if (pstack->top == NULL) {
		return NULL;
	}else {
		strcpy(str, pstack->top->str);
		if (p == NULL) {
			free(pstack->top);
			pstack->top = NULL;
			pstack->button = NULL;
		}
		else {//最后一个的特殊情况
			free(pstack->top);
			pstack->top = p;
			p->next = NULL;
		}
		return str;
	}
}


void printStack(Stack* pstack) {
	if (pstack->button == NULL) {
		printf("Stack is empty!\n");
		return;
	}

	Node* current = pstack->button; // 从栈底开始遍历
	printf("Stack elements: ");
	while (current != NULL) {
		printf("%s->", current->str); // 输出当前节点的值
		current = current->next; // 移动到下一个节点
	}
	printf("NULL\n");
}


//获取小数
double double_value(char *str) {
	double num = 0;
	int len = strlen(str);
	int t = 0;
	for (int i = 0; i < len; i++) {
		if (str[i] != '.') {
			num = num * 10 + (str[i] - '0');
		}else {
			t = i;
		}
	}
	if (t == 0) {
		return num;
	}
	num = num / pow(10, len - t - 1);
	return num;
}


//截取字符串
char* getstr(char *str1, char *str, int t1, int t2) {
	int t = 0;
	for (int i = t1; i < t2; i++) {
		str1[t] = str[i];
		t++;
	}
	str1[t] = '\0';
	return str1;
}


//获取单个字符
char* get_char(char* str, char* aim, int i) {
	aim[0] = str[i];
	aim[1] = '\0';
	return aim;
}


//获取后缀表达式
char* getresult(char *str, char *strresult, Stack *pstack) {
	int len = strlen(str);
	int t1 = 0;
	int t2 = 0;
	//获取数字或符号
	char s[10];
	for (t2 = 0; t2 < len; t2++) {
		//printf("传入%c\n", str[t2]);
		if (str[t2] == '*'|| str[t2] == '/'|| str[t2] == '(') {
			get_char(str, s, t2);
			push(pstack, s);
			//printf("c\n");
		}else if (str[t2] == ')') {
			char a[5];
			//get_char(str, a, t2);
			while (1) {
				if (strcmp(pstack->top->str, "(") == 0) {
					pop(a, pstack);
					break;
				}
				else{
					pop(a, pstack);
					strcat(strresult, a);
					strcat(strresult, ",");
				}
			}
			//printf("过了\n");
		}else if (str[t2] >= '0' && str[t2] <= '9') {
			char a[10];
			for (t1 = t2; t1 < len; t1++) {
				if (!(str[t1] >= '0' && str[t1] <= '9' )) {
					if (str[t1] != '.') {
						break;
					}
				}
			}
			getstr(a, str, t2, t1);
			strcat(strresult, a);
			strcat(strresult, ",");
			t2 = t1 - 1;
			//printf("t1 = %d,t2 = %d\n", t1, t2);
		}else if (str[t2] == '+' || str[t2] == '-') {
			char a[3];
			get_char(str, a, t2);
			//printf("%s", a);
			while (1) {
				if (pstack->top == NULL) {
					push(pstack, a);
					//printf("a\n");
					//printf("-------");
					break;
				}
				else if (strcmp(pstack->top->str, "(") == 0) {
					push(pstack, a);
					break;
				}
				else {
					char s1[10];
					pop(s1, pstack);
					strcat(strresult, s1);
					strcat(strresult, ",");
				}
			}
		}
		//printStack(pstack);
	}
	while (pstack->top != NULL) {
		char f[5];
		pop(f, pstack);
		strcat(strresult, f);
		strcat(strresult, ",");
	}
}

char* getanswer(char *numstr, char* result, Stack* pstack) {
	int len = strlen(result);
	int t1;
	int t2 = 0;
	for (t1 = 0; t1 < len; t1++) {
		char n[10];
		if (result[t1] >= '0' && result[t1] <= '9') {
			for (t2 = t1; t2 < len; t2++) {
				if (result[t2] == ',') {
					break;
				}
			}
			getstr(n, result, t1, t2);
			push(pstack, n);
		}
		else if (result[t1] == '+' || result[t1] == '-' || result[t1] == '*' || result[t1] == '/') {
			double num1 = 0;
			double num2 = 0;
			char numstr1[20];
			char numstr2[20];
			pop(numstr1, pstack);
			pop(numstr2, pstack);
			num1 = double_value(numstr1);
			num2 = double_value(numstr2);
			double num3 = 0;
			if (result[t1] == '+') {
				num3 = num2 + num1;
			}
			else if (result[t1] == '-') {
				num3 = num2 - num1;
			}
			else if (result[t1] == '*')
			{
				num3 = num2 * num1;
			}
			else if (result[t1] == '/') {
				num3 = num2 / num1;
			}
			//printf("%.6f\n", num3);
			sprintf(numstr, "%.6f", num3);
			push(pstack, numstr);
		}
	}
	char m[20];
	pop(m, pstack);
	return m;
}



int main() {
	Stack stack;
	start(&stack);
	char str[40] = "";
	while (1) {
		printf("请输入算式（算式之间不要有等号，后面不需要加=），输入1退出\n");
		scanf("%s", str);
		if (strcmp(str, "1") == 0) {
			printf("程序结束\n");
			break;
		}else{
			char result[40];
			char number[20];
			getresult(str, result, &stack);
			getanswer(number, result, &stack);
			printf("%s", number);
			getchar();
			getchar();
		}
	}
	/*
	push(&stack, "123");
	push(&stack, "123");
	push(&stack, "123");
	while (stack.top != NULL) {
		pop(str, &stack);
	}
	printStack(&stack);
	*/
	return 0;
}
