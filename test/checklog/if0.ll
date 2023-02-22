
define noundef i32 @f(i32 noundef %0, i32 noundef %1, i1 noundef %2) #0 {
	br i1 %2, label %add, label %sub
add:
	%4 = add i32 %0, %1
	ret i32 %4
sub:
	%5 = sub i32 %0, %1
	ret i32 %5
}


attributes #0 = { nounwind }