.model small
.radix 10
.stack 200h
.data
;         aquí viene el segmento de identificadores variables y constantes.
    ; DATOS DE LA TABLA DE SIMBOLOS 
    string7       db      ' fin del programa ',10,13,'$'
    
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
            

            ; 0 [ (tolong) ,   ,1]  type : long

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 1
             push ebx
            

            ; 1 [ (:=) , k_0_0  , [ 0 ]]  type : long

             pop   edx
             mov  k_0_0,  edx
            

            ; 2 [ (+) , k_0_0  ,1]  type : long

            label2:
             ;  // SUMA.
             mov ecx, k_0_0
             add ecx, 1
            push ecx
            

            ; 3 [ (:=) , k_0_0  , [ 2 ]]  type : long

             pop   edx
             mov  k_0_0,  edx
            

            ; 4 [ (tolong) ,   ,4]  type : long

            ;  // TOLONG VARIABLE 
             ;                        la varaible es uint.
             xor ebx,  ebx
             mov bx, 4
             push ebx
            

            ; 5 [ (<) , k_0_0  , [ 4 ]]  type : long

             mov ebx,  k_0_0
             pop edx
             cmp ebx,  edx
            

            ; 6 [ (BF) ,  [ 8 ]   ,]  type : long

            jge    label8
            

            ; 7 [ (BI) ,  [ 2 ]   ,]  type : long

            jmp    label2
            

            ; 8 [ (print) ,   , fin del programa ]  type : 

            label8:
            mov ah, 09
            lea dx, string7
            int 21h
            

        exit_code:
                              mov ah,04ch
                              mov al,00h
                              int 21h
        end start  
        
  
