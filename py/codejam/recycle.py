#! /usr/bin/env python

import sys

def solve(A, B):
	if(A <= 9 and B <= 9):
		return 0;

	answers = 0
	n = A
	m = B

	ranges = range(A, B)

	for i in range(0, len(ranges)):
		n = ranges[i]
		m = ranges[i]

		for j in range(len(str(n))):
			m_str = str(m)
			m_str = m_str[-1] + m_str[0:-1]
			m = int(m_str)

			if(m <= B and m > n and m >= A):
				answers += 1

	return answers


f = open('/tmp/codejam', 'r')

cases = int(f.readline())

arr = []

for x in range(0, cases):
	arr.append(f.readline())

case = 0;

for line in arr:
	line = line.strip()
	numbers = line.split(' ')

	case += 1
	answer = "Case #" + str(case) + ": "
	
	if(len(numbers) != 2):
		answer += '0'
		print answer
		continue
	
	answer  += str(solve(int(numbers[0]), int(numbers[1])))

	print answer
