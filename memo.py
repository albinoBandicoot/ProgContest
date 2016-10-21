import sys

# a cute little thing that will automatically create a memoized
# version of any function, with a minimal modification to the 
# original function: instead of calling itself recursively, it
# calls a 'memo' object that is its first parameter.

# unfortunately, this is still recursive, and Python's stack depth
# limit is rather small (the string compression example ran out of
# stack space with under 500 stack frames, or about 125 calls), so
# this is probably not a viable replacement for actually doing the
# dynamic programming. But I love how Python lets you do something
# like this in 10 lines.

# here's the auto-memoizer:
class memo:
	def __init__ (self, f):
		self.d = {}
		self.f = lambda args : f(self,*args)
	def __call__ (self, *arg):
		if arg in self.d:
			return self.d[arg]
		res = self.f(arg)
		self.d[arg] = res
		return res

# example: fibonacci sequence
def fib (memo,n):
	if n <= 1:
		return 1
	return memo(n-1) + memo(n-2)

# example: minimum edit distance
def minEditDist (m, s1, s2):
	if s1 == s2:
		return 0
	if len(s1) == 0:
		return len(s2)
	if len(s2) == 0:
		return len(s1)
	if s1[0] == s2[0]:
		return m(s1[1:], s2[1:])
	return min(2+m(s1[1:], s2[1:]), 1+m(s1[1:], s2), 1+m(s1, s2[1:]))


# example: string compression: can replace ababab with 3[ab]
# or abababcabababc with 2[3[ab]c]
def check(s,d):
	return all((s[:d] == s[i:i+d] for i in range(0,len(s),d)))

def parens(d):
	if d == 1:
		return 0
	return 2

def strCompress (m, s):
	if len(s) <= 2:
		return len(s)
	sm = min((m(s[:i]) + m(s[i:]) for i in range(1,len(s))))
	n = len(s)
	inds = filter (lambda d : n%d == 0 and check(s,d), range(1,n/2+1))
	vals = [m(s[:d]) + len(str(n/d)) + parens(d) for d in inds]
	if (len(inds) > 0):
		print s, list(zip(inds,vals))
	vals.append(sm)
	return min(vals)

# create the memo object
h = memo(minEditDist)
# call it as you would the unmodified function
print h("stead", "slated")
