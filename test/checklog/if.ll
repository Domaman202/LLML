
define noundef i32 @f(i32 noundef %a, i32 noundef %b, i1 noundef %act) #0 {
	br i1 %act, label %add, label %sub
add:
	%1 = add i32 %a, %b
	ret i32 %1
sub:
	%2 = sub i32 %a, %b
	ret i32 %2
}


attributes #0 = { nounwind }