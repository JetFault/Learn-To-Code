#! /usr/bin/env python

import sys

keys = {'a':'y', 'b':'h', 'c':'e', 'd':'s', 'e':'o', 'f':'c', 'g':'v', 'h':'x', 'i':'d', 'j':'u', 'k':'i', 'l':'g', 'm':'l', 'n':'b', 'o':'k', 'p':'r', 'q':'z', 'r':'t', 's':'n', 't':'w', 'u':'j', 'v':'p', 'w':'f', 'x':'m', 'y':'a', 'z':'q', ' ':' ', '\n':'\n'}

f = open('/tmp/codejamA', 'r')

cases = int(f.readline())

arr = []

for x in range(0, cases):
	arr.append(f.readline())

case = 0;

for line in arr:
	case += 1
	answer = "Case #" + str(case) + ": "
	for i in range(0, len(line)):
		answer += keys[line[i]]

	print answer
