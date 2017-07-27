.model small
.radix 10
.stack 200h
.data
;         aquí viene el segmento de identificadores variables y constantes.
     DATOS DE LA TABLA DE SIMBOLOS 
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
    string36       db      ' Fin del programa',10,13,'$'
    
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
            

            ; 0 [ (:=) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 1 TYPE: uint> --type : <uint>]

             mov  dx,  1
             mov  i_0_0,  dx
            

            ; 1 [ (print)    ---   < TOKEN: string LEXEME:  mensaje dentro del while TYPE: string> --type : <>]

            label1:
            mov ah, 09
            lea dx, string9
            int 21h
            

            ; 2 [ (+) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 1 TYPE: uint> --type : <uint>]

             ;  // SUMA.
             mov cx, i_0_0
             add cx, 1
            push cx
            

            ; 3 [ (:=) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---    [ 2 ] --type : <uint>]

             pop   dx
             mov  i_0_0,  dx
            

            ; 4 [ (<) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 100 TYPE: uint> --type : <uint>]

             mov bx,  i_0_0
             cmp bx,  100
            

            ; 5 [ (BF)  [ 7 ]    ---    --type : <uint>]

            jge    label7
            

            ; 6 [ (BI)  [ 1 ]    ---    --type : <>]

            jmp    label1
            

            ; 7 [ (print)    ---   < TOKEN: string LEXEME: i=10,saliendo del while TYPE: string> --type : <>]

            label7:
            mov ah, 09
            lea dx, string12
            int 21h
            

            ; 8 [ (:=) < TOKEN: identifier LEXEME: index_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 0 TYPE: uint> --type : <uint>]

             mov  dx,  0
             mov  index_0_0,  dx
            

            ; 9 [ (/) < TOKEN: const LEXEME: 44 TYPE: uint>   ---   < TOKEN: const LEXEME: 4 TYPE: uint> --type : <uint>]

             xor  dx,  dx
             mov  ax,  44
             mov  bx,  4
            cmp  bx, 0h
            jz errDivByZero
            div    bx
            push ax
            

            ; 10 [ (*)  [ 9 ]    ---   < TOKEN: identifier LEXEME: index_0_0 TYPE: uint> --type : <uint>]

             pop  ax
             mov  cx,   index_0_0
            mul    cx
            push ax
            

            ; 11 [ (-) < TOKEN: const LEXEME: 45 TYPE: uint>   ---    [ 10 ] --type : <uint>]

             ;    // RESTA.
             pop  bx
             mov  cx,  45
             sub cx, bx
             cmp  cx,0h 
            jl errSub
             push cx
            

            ; 12 [ (-)  [ 11 ]    ---   < TOKEN: const LEXEME: 44 TYPE: uint> --type : <uint>]

             ;    // RESTA.
             pop   cx
             sub cx, 44
             cmp  cx,0h 
            jl errSub
             push cx
            

            ; 13 [ (tolong)    ---    [ 12 ] --type : <long>]

            ;    // TOLONG REGISTER 
             pop  bx;            sacando el registro a extender
             ;                        el registro es uint.
             xor    ecx,   ecx
             mov    cx, bx
            mov ebx,  ecx
             push ebx
            

            ; 14 [ (:=) < TOKEN: identifier LEXEME: w_1_1 TYPE: long>   ---    [ 13 ] --type : <long>]

             pop   edx
             mov  w_1_1,  edx
            

            ; 15 [ (:=) < TOKEN: identifier LEXEME: a_1_1 TYPE: uint>   ---   < TOKEN: const LEXEME: 7 TYPE: uint> --type : <uint>]

             mov  dx,  7
             mov  a_1_1,  dx
            

            ; 16 [ (print)    ---   < TOKEN: string LEXEME:  do while dentro de un ambito TYPE: string> --type : <>]

            label16:
            mov ah, 09
            lea dx, string28
            int 21h
            

            ; 17 [ (:=) < TOKEN: identifier LEXEME: d_1_1 TYPE: long>   ---   < TOKEN: const LEXEME: -1 TYPE: long> --type : <long>]

             mov  edx,  -1
             mov  d_1_1,  edx
            

            ; 18 [ (:=) < TOKEN: identifier LEXEME: f_1_1 TYPE: long>   ---   < TOKEN: const LEXEME: -10 TYPE: long> --type : <long>]

             mov  edx,  -10
             mov  f_1_1,  edx
            

            ; 19 [ (-) < TOKEN: identifier LEXEME: d_1_1 TYPE: long>   ---   < TOKEN: identifier LEXEME: f_1_1 TYPE: long> --type : <long>]

             ;    // RESTA.
             mov ecx, d_1_1
             sub ecx, f_1_1
             push ecx
            

            ; 20 [ (:=) < TOKEN: identifier LEXEME: i_1_1 TYPE: long>   ---    [ 19 ] --type : <long>]

             pop   edx
             mov  i_1_1,  edx
            

            ; 21 [ (tolong)    ---   < TOKEN: identifier LEXEME: w_1_1 TYPE: long> --type : <long>]

            ;  // TOLONG VARIABLE 
             mov ebx, w_1_1
             push ebx
            

            ; 22 [ (tolong)    ---   < TOKEN: identifier LEXEME: index_0_0 TYPE: uint> --type : <long>]

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, index_0_0
             push ebx
            

            ; 23 [ (+)  [ 21 ]    ---    [ 22 ] --type : <long>]

             ;  // SUMA.
             pop ebx
             pop ecx
            add  ecx, ebx
            push ecx
            

            ; 24 [ (>) < TOKEN: identifier LEXEME: i_1_1 TYPE: long>   ---    [ 23 ] --type : <long>]

             mov ebx,  i_1_1
             pop edx
             cmp ebx,  edx
            

            ; 25 [ (BF)  [ 28 ]    ---    --type : <long>]

            jle    label28
            

            ; 26 [ (print)    ---   < TOKEN: string LEXEME: es verdad:i > tolong(0)+tolong(index) TYPE: string> --type : <>]

            mov ah, 09
            lea dx, string31
            int 21h
            

            ; 27 [ (BI)  [ 29 ]    ---    --type : <uint>]

            jmp    label29
            

            ; 28 [ (print)    ---   < TOKEN: string LEXEME: es falso: i > tolong(0)+tolong(index) TYPE: string> --type : <>]

            label28:
            mov ah, 09
            lea dx, string32
            int 21h
            

            ; 29 [ (+) < TOKEN: identifier LEXEME: index_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 1 TYPE: uint> --type : <uint>]

            label29:
             ;  // SUMA.
             mov cx, index_0_0
             add cx, 1
            push cx
            

            ; 30 [ (:=) < TOKEN: identifier LEXEME: index_0_0 TYPE: uint>   ---    [ 29 ] --type : <uint>]

             pop   dx
             mov  index_0_0,  dx
            

            ; 31 [ (:=) < TOKEN: identifier LEXEME: b_1_1 TYPE: uint>   ---   < TOKEN: identifier LEXEME: index_0_0 TYPE: uint> --type : <uint>]

             mov  dx,  index_0_0
             mov  b_1_1,  dx
            

            ; 32 [ (-) < TOKEN: identifier LEXEME: a_1_1 TYPE: uint>   ---   < TOKEN: identifier LEXEME: b_1_1 TYPE: uint> --type : <uint>]

             ;    // RESTA.
             mov cx, a_1_1
             sub cx, b_1_1
             cmp  cx,0h 
            jl errSub
             push cx
            

            ; 33 [ (:=) < TOKEN: identifier LEXEME: b_1_1 TYPE: uint>   ---    [ 32 ] --type : <uint>]

             pop   dx
             mov  b_1_1,  dx
            

            ; 34 [ (/) < TOKEN: identifier LEXEME: a_1_1 TYPE: uint>   ---   < TOKEN: const LEXEME: 0 TYPE: uint> --type : <uint>]

             xor  dx,  dx
             mov  ax,  a_1_1
             mov  bx,  0
            cmp  bx, 0h
            jz errDivByZero
            div    bx
            push ax
            

            ; 35 [ (:=) < TOKEN: identifier LEXEME: a_1_1 TYPE: uint>   ---    [ 34 ] --type : <uint>]

             pop   dx
             mov  a_1_1,  dx
            

            ; 36 [ (<) < TOKEN: identifier LEXEME: index_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 9 TYPE: uint> --type : <uint>]

             mov bx,  index_0_0
             cmp bx,  9
            

            ; 37 [ (BF)  [ 39 ]    ---    --type : <uint>]

            jge    label39
            

            ; 38 [ (BI)  [ 16 ]    ---    --type : <>]

            jmp    label16
            

            ; 39 [ (print)    ---   < TOKEN: string LEXEME:  Fin del programa TYPE: string> --type : <>]

            label39:
            mov ah, 09
            lea dx, string36
            int 21h
            

        exit_code:
                              mov ah,04ch
                              mov al,00h
                              int 21h
        end start  
        
  
