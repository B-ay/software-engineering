1.puzzle文件夹为题目，ans文件夹为答案，同一组题目和答案使用了相同名称。
2.文件名与内容说明如下：
	文件名			内容说明			题目数量
	add_decimal		绝对值20以内小数加法	100
	add_under_100		100以内正数加法		100
	add_under_zero		绝对值100以内加法		100
	sub_decimal		绝对值20以内小数减法	100
	sub_under_100		100以内正数减法		100
	sub_under_zero		绝对值100以内减法		100
	nine_to_nine		九九乘法表		81
	multiplication_easy	两位数乘一位数乘法	30
	multiplication_hard	两位数乘两位数乘法	30
3.通过set保证了生成的题目互不相同。
4.如果题目中的第二个计算数字出现了负数，输出中加了括号，读取文件时注意识别。

