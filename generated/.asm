.model small
.radix 10
.stack 200h
.data
;         aquí viene el segmento de identificadores variables y constantes.
i_0_0    dw         0   ;      identificador  uint
j_0_0    dw         0   ;      identificador  uint
k_0_0    dw         0   ;      identificador  uint
a_0_0    dd         0   ;      identificador  long
b_0_0    dd         0   ;      identificador  long
string5       db      ' comienzo del programa',10,13,'$'
string8       db      '-----bucle externo',10,13,'$'
string11       db      ' ---------------------bucle anidado middle',10,13,'$'
string14       db      '--------------------anidado mas deep',10,13,'$'
string18       db      ' resta final',10,13,'$'
string19       db      'fin del programa',10,13,'$'

;          Chequeos en tiempo de ejecución:

runtimeExceptionDivByZero            db      '[Error]: No se puede dividir por 0.',10,13,'$'
runtimeExceptionSubOp           db      '[Error]: Sustracción  fuera de rango.',10,13,'$'
.code
.386
.387
start:
                  mov ax,@data 
                  mov ds,ax   
                  jmp start_code
errDivByZero:
                  mov ah,09 
                  mov dx, offset runtimeExceptionDivByZero
                  int 21h 
                  jmp exit_code 
errSub:
                  mov ah,09 
                  mov dx, offset runtimeExceptionSubOp
                  int 21h 
                  jmp exit_code 
start_code:


mov ah, 09
lea dx, String5
int 21h


 mov  dx,  5
 mov  j_0_0,  dx


 ;                                                        Resta.
 mov cx, j_0_0
 sub cx, 1
 cmp  cx,0h 
jl errSub
 push cx


 pop   dx
 mov  j_0_0,  dx


mov ah, 09
lea dx, String8
int 21h


 mov  dx,  1
 mov  i_0_0,  dx


 ;                                                        suma.
 mov ecx, a_0_0
 add ecx, 1
push ecx
                            ; ( + , ecx,  ebx)


label7:
 pop   edx
 mov  a_0_0,  edx


label8:
mov ah, 09
lea dx, String11
int 21h


 mov  dx,  5
 mov  k_0_0,  dx


label10:
 mov  dx,  1
 mov  i_0_0,  dx


mov ah, 09
lea dx, String14
int 21h


 mov bx,  k_0_0
 cmp bx,  0


jbe    label15


jmp    label10


label15:
 mov bx,  i_0_0
 cmp bx,  5


ja    label18


jmp    label8


label18:
 mov bx,  j_0_0
 cmp bx,  0


jbe    label21


jmp    label7


label21:
mov ah, 09
lea dx, String18
int 21h


 ;                                                        Resta.
 mov ecx, a_0_0
 sub ecx, b_0_0
 push ecx


 pop   edx
 mov  a_0_0,  edx


mov ah, 09
lea dx, String19
int 21h


exit_code:
                  mov ah,04ch
                  mov al,00h
                  int 21h
end start  

  
