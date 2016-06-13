.data
a_Global : 0

vetor_Global: 0,0,0
.text
LDI		0
STO		1000
LDI		1
STO		$indr
LDV		vetor
ADDI	1
STO		1001
LD		1000
STO		$indr
LD		1001
STOV	vetor_Global
HLT		0
