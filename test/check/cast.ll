target triple = "x86_64-pc-linux-gnu"

define noundef float @f(i8 noundef %i0) #0 {
	%1 = sext i8 %i0 to i64
	%i1 = bitcast i64 %1 to i64
	%2 = sitofp i64 %i1 to float
	ret float %2
}

define noundef i64 @iu(i8 noundef %i) #0 {
	%1 = sext i8 %i to i64
	ret i64 %1
}

define noundef i8 @id(i64 noundef %i) #0 {
	%1 = trunc i64 %i to i8
	ret i8 %1
}

define noundef float @itf(i32 noundef %i) #0 {
	%1 = sitofp i32 %i to float
	ret float %1
}

define noundef i32 @fti(float noundef %f) #0 {
	%1 = fptosi float %f to i32
	ret i32 %1
}

define noundef double @fu(half noundef %f) #0 {
	%1 = fpext half %f to double
	ret double %1
}

define noundef half @fd(double noundef %f) #0 {
	%1 = fptrunc double %f to half
	ret half %1
}

attributes #0 = { nounwind }