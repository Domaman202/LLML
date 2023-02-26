target triple = "x86_64-pc-linux-gnu"

define noundef float @f() #0 {
	%1 = fdiv float 21.0, 100.0
	%2 = fadd float 12.0, %1
	ret float %2
}

attributes #0 = { nounwind }