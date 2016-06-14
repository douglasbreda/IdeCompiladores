.data

vetor_Global: 0,0,0
a_Global : 0
b_Global : 0
.text
LDI		3
STO		a_Global
LDI		4
STO		b_Global
LDI		0
STO		1000
LD		a_Global
ADD		b_Global
STO		1001
LD		1000
STO		$indr
LD		1001
STOV	vetor_Global
HLT		0
