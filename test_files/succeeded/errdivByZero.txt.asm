.model small
.radix 10
.stack 200h
.data
;         aquí viene el segmento de identificadores variables y constantes.
a_0_0    dd         0   ;      identificador  long
b_0_0    dd         0   ;      identificador  long
c_0_0    dd         0   ;      identificador  long
d_0_0    dw         0   ;      identificador  uint
e_0_0    dw         0   ;      identificador  uint
f_0_0    dw         0   ;      identificador  uint
i_0_0    dw         0   ;      identificador  uint
index_0_0    dw         0   ;      identificador  uint
string9       db      ' mensaje dentro del while',10,13,'$'
string12       db      'i=10,saliendo del while',10,13,'$'
d_1_1    dd         0   ;      identificador  long
e_1_1    dd         0   ;      identificador  long
f_1_1    dd         0   ;      identificador  long
i_1_1    dd         0   ;      identificador  long
w_1_1    dd         0   ;      identificador  long
a_1_1    dw         0   ;      identificador  uint
b_1_1    dw         0   ;      identificador  uint
c_1_1    dw         0   ;      identificador  uint
index_1_1    dw         0   ;      identificador  uint
string28       db      ' do while dentro de un ambito',10,13,'$'
string31       db      'es verdad:i > tolong(0)+tolong(index)',10,13,'$'
string32       db      'es falso: i > tolong(0)+tolong(index)',10,13,'$'
string36       db      ' segunda parte del programa',10,13,'$'

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


 mov  dx,  1
 mov  i_0_0,  dx


label1:
mov ah, 09
lea dx, String9
int 21h


 ;                                                        suma.
 mov cx, i_0_0
 add cx, 1
push cx
                            ; ( + , cx,  bx)


 pop   dx
 mov  i_0_0,  dx


 mov bx,  i_0_0
 cmp bx,  100


jae    label7


jmp    label1


label7:
mov ah, 09
lea dx, String12
int 21h


 mov  dx,  0
 mov  index_0_0,  dx


 xor  dx,  dx
 mov  ax,  44
 mov  bx,  4
cmp  bx, 0h
jz errDivByZero
div    bx
push ax


 pop  ax
 mov  cx,   index_0_0
mul    cx
push ax


 ;                                                        Resta.
 pop  bx
 mov  cx,  45
 sub cx, bx
 cmp  cx,0h 
jl errSub
 push cx


 ;                                                        Resta.
 pop   cx
 sub cx, 44
 cmp  cx,0h 
jl errSub
 push cx


;                                         tolong register 
 pop  bx;  sacando el registro a extender
 push bx


 pop   edx
 mov  w_1_1,  edx


 mov  dx,  7
 mov  a_1_1,  dx


label16:
mov ah, 09
lea dx, String28
int 21h


 mov  edx,  -1
 mov  d_1_1,  edx


 mov  edx,  -10
 mov  f_1_1,  edx


 ;                                                        Resta.
 mov ecx, d_1_1
 sub ecx, f_1_1
 push ecx


 pop   edx
 mov  i_1_1,  edx


;           tolong variable 
 mov ebx, w_1_1
 push ebx


;           tolong variable 
 ;                        la varaible es uint.
 xor ebx,  ebx
 mov bx, index_0_0
 push ebx


 ;                                                        suma.
 pop ebx
 pop ecx
add  ecx, ebx
push ecx
                            ; ( + , ecx,  ebx)


 mov ebx,  i_1_1
 pop edx
 cmp ebx,  edx


jle    label28


mov ah, 09
lea dx, String31
int 21h


jmp    label29


label28:
mov ah, 09
lea dx, String32
int 21h


label29:
 ;                                                        suma.
 mov cx, index_0_0
 add cx, 1
push cx
                            ; ( + , cx,  bx)


 pop   dx
 mov  index_0_0,  dx


 mov  dx,  index_0_0
 mov  b_1_1,  dx


 ;                                                        Resta.
 mov cx, a_1_1
 sub cx, b_1_1
 cmp  cx,0h 
jl errSub
 push cx


 pop   dx
 mov  b_1_1,  dx


 xor  dx,  dx
 mov  ax,  a_1_1
 mov  bx,  0
cmp  bx, 0h
jz errDivByZero
div    bx
push ax


 pop   dx
 mov  a_1_1,  dx


 mov bx,  index_0_0
 cmp bx,  9


jae    label39


jmp    label16


label39:
mov ah, 09
lea dx, String36
int 21h


exit_code:
                  mov ah,04ch
                  mov al,00h
                  int 21h
end start  

  
