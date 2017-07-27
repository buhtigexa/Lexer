.model small
.radix 10
.stack 200h
.data
;         aquí viene el segmento de identificadores variables y constantes.
     DATOS DE LA TABLA DE SIMBOLOS 
    i_0_0    dw         0   ;      identificador  uint
    a_0_0    dd         0   ;      identificador  long
    b_0_0    dd         0   ;      identificador  long
    j_0_0    dd         0   ;      identificador  long
    k_0_0    dd         0   ;      identificador  long
    string9       db      'bucle externo',10,13,'$'
    string12       db      'bucle anidado middle',10,13,'$'
    string14       db      'anidado mas deep',10,13,'$'
    string18       db      ' resta final',10,13,'$'
    string21       db      ' fin del programa ',10,13,'$'
    
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
            

            ; 0 [ (:=) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 70000 TYPE: long> --type : <long>]

             mov  edx,  70000
             mov  j_0_0,  edx
            

            ; 1 [ (:=) < TOKEN: identifier LEXEME: k_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 65536 TYPE: long> --type : <long>]

             mov  edx,  65536
             mov  k_0_0,  edx
            

            ; 2 [ (:=) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 2 TYPE: uint> --type : <uint>]

             mov  dx,  2
             mov  i_0_0,  dx
            

            ; 3 [ (+) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 1 TYPE: long> --type : <long>]

             ;  // SUMA.
             mov ecx, j_0_0
             add ecx, 1
            push ecx
            

            ; 4 [ (:=) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---    [ 3 ] --type : <long>]

             pop   edx
             mov  j_0_0,  edx
            

            ; 5 [ (tolong)    ---   < TOKEN: const LEXEME: 3 TYPE: uint> --type : <long>]

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 3
             push ebx
            

            ; 6 [ (+) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---    [ 5 ] --type : <long>]

             ;  // SUMA.
             pop  ecx
             add   ecx, j_0_0
            push ecx
            

            ; 7 [ (:=) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---    [ 6 ] --type : <long>]

             pop   edx
             mov  j_0_0,  edx
            

            ; 8 [ (print)    ---   < TOKEN: string LEXEME: bucle externo TYPE: string> --type : <>]

            label8:
            mov ah, 09
            lea dx, string9
            int 21h
            

            ; 9 [ (+) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 1 TYPE: long> --type : <long>]

             ;  // SUMA.
             mov ecx, j_0_0
             add ecx, 1
            push ecx
            

            ; 10 [ (:=) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---    [ 9 ] --type : <long>]

             pop   edx
             mov  j_0_0,  edx
            

            ; 11 [ (tolong)    ---   < TOKEN: const LEXEME: 3 TYPE: uint> --type : <long>]

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 3
             push ebx
            

            ; 12 [ (+) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---    [ 11 ] --type : <long>]

             ;  // SUMA.
             pop  ecx
             add   ecx, j_0_0
            push ecx
            

            ; 13 [ (:=) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---    [ 12 ] --type : <long>]

             pop   edx
             mov  j_0_0,  edx
            

            ; 14 [ (:=) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 1 TYPE: uint> --type : <uint>]

             mov  dx,  1
             mov  i_0_0,  dx
            

            ; 15 [ (print)    ---   < TOKEN: string LEXEME: bucle anidado middle TYPE: string> --type : <>]

            label15:
            mov ah, 09
            lea dx, string12
            int 21h
            

            ; 16 [ (+) < TOKEN: identifier LEXEME: a_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 1 TYPE: long> --type : <long>]

             ;  // SUMA.
             mov ecx, a_0_0
             add ecx, 1
            push ecx
            

            ; 17 [ (:=) < TOKEN: identifier LEXEME: a_0_0 TYPE: long>   ---    [ 16 ] --type : <long>]

             pop   edx
             mov  a_0_0,  edx
            

            ; 18 [ (:=) < TOKEN: identifier LEXEME: a_0_0 TYPE: long>   ---   < TOKEN: identifier LEXEME: a_0_0 TYPE: long> --type : <long>]

             mov  edx,  a_0_0
             mov  a_0_0,  edx
            

            ; 19 [ (tolong)    ---   < TOKEN: const LEXEME: 5 TYPE: uint> --type : <long>]

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 5
             push ebx
            

            ; 20 [ (:=) < TOKEN: identifier LEXEME: k_0_0 TYPE: long>   ---    [ 19 ] --type : <long>]

             pop   edx
             mov  k_0_0,  edx
            

            ; 21 [ (print)    ---   < TOKEN: string LEXEME: anidado mas deep TYPE: string> --type : <>]

            label21:
            mov ah, 09
            lea dx, string14
            int 21h
            

            ; 22 [ (+) < TOKEN: identifier LEXEME: k_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 1 TYPE: long> --type : <long>]

             ;  // SUMA.
             mov ecx, k_0_0
             add ecx, 1
            push ecx
            

            ; 23 [ (:=) < TOKEN: identifier LEXEME: k_0_0 TYPE: long>   ---    [ 22 ] --type : <long>]

             pop   edx
             mov  k_0_0,  edx
            

            ; 24 [ (>) < TOKEN: identifier LEXEME: k_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 100000 TYPE: long> --type : <long>]

             mov ebx,  k_0_0
             cmp ebx,  100000
            

            ; 25 [ (BF)  [ 27 ]    ---    --type : <long>]

            jle    label27
            

            ; 26 [ (BI)  [ 21 ]    ---    --type : <>]

            jmp    label21
            

            ; 27 [ (<=) < TOKEN: identifier LEXEME: i_0_0 TYPE: uint>   ---   < TOKEN: const LEXEME: 5 TYPE: uint> --type : <uint>]

            label27:
             mov bx,  i_0_0
             cmp bx,  5
            

            ; 28 [ (BF)  [ 30 ]    ---    --type : <uint>]

            jg    label30
            

            ; 29 [ (BI)  [ 15 ]    ---    --type : <>]

            jmp    label15
            

            ; 30 [ (<) < TOKEN: identifier LEXEME: j_0_0 TYPE: long>   ---   < TOKEN: const LEXEME: 200000 TYPE: long> --type : <long>]

            label30:
             mov ebx,  j_0_0
             cmp ebx,  200000
            

            ; 31 [ (BF)  [ 33 ]    ---    --type : <long>]

            jge    label33
            

            ; 32 [ (BI)  [ 8 ]    ---    --type : <>]

            jmp    label8
            

            ; 33 [ (print)    ---   < TOKEN: string LEXEME:  resta final TYPE: string> --type : <>]

            label33:
            mov ah, 09
            lea dx, string18
            int 21h
            

            ; 34 [ (tolong)    ---   < TOKEN: const LEXEME: 2 TYPE: uint> --type : <long>]

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 2
             push ebx
            

            ; 35 [ (:=) < TOKEN: identifier LEXEME: b_0_0 TYPE: long>   ---    [ 34 ] --type : <long>]

             pop   edx
             mov  b_0_0,  edx
            

            ; 36 [ (tolong)    ---   < TOKEN: const LEXEME: 1 TYPE: uint> --type : <long>]

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 1
             push ebx
            

            ; 37 [ (:=) < TOKEN: identifier LEXEME: a_0_0 TYPE: long>   ---    [ 36 ] --type : <long>]

             pop   edx
             mov  a_0_0,  edx
            

            ; 38 [ (+) < TOKEN: identifier LEXEME: a_0_0 TYPE: long>   ---   < TOKEN: identifier LEXEME: b_0_0 TYPE: long> --type : <long>]

             ;  // SUMA.
             mov ecx, a_0_0
             add ecx, b_0_0
            push ecx
            

            ; 39 [ (:=) < TOKEN: identifier LEXEME: a_0_0 TYPE: long>   ---    [ 38 ] --type : <long>]

             pop   edx
             mov  a_0_0,  edx
            

            ; 40 [ (print)    ---   < TOKEN: string LEXEME:  fin del programa  TYPE: string> --type : <>]

            mov ah, 09
            lea dx, string21
            int 21h
            

        exit_code:
                              mov ah,04ch
                              mov al,00h
                              int 21h
        end start  
        
  
