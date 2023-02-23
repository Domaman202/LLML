target triple = "x86_64-pc-linux-gnu"

define noundef i32 @f(i32 noundef %0) #0 {
	%2 = icmp slt i32 %0, 5
	br i1 %2, label %ret5, label %retI
ret5:
	ret i32 5
retI:
	ret i32 %0
}


attributes #0 = { nounwind }