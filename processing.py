import numpy as np
import multiprocessing
import time

n_workers = 8

count = 512 * 1024 * 1024

start = time.time()
numbers = np.memmap('numbers.txt', dtype=np.uint32)
delt = int(count / n_workers)
ls = (numbers[i:i + delt] for i in range(0, count, delt))

with multiprocessing.Pool(n_workers) as executor:
    arr = executor.map(sum, ls)

print(sum(arr))

end = time.time()
print('MMap на 8 процессов: ', end - start)

start = time.time()
numbers = np.fromfile('numbers.txt', dtype=np.uint32)
ls = (numbers[i:i + delt] for i in range(0, count, delt))

with multiprocessing.Pool(n_workers) as executor:
    arr = executor.map(sum, ls)

print(sum(arr))

end = time.time()
print('Без MMap на 8 процессов: ', end - start)
