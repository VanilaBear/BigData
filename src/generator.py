import random
import time


def write_bytes(filename):
    with open(filename, 'wb') as file:
        for i in range(536870912):
            n = random.randint(0, 4294967295)
            file.write(n.to_bytes(4, byteorder='big'))


def read_bytes(filename):
    min_number = 4294967295
    max_number = 0
    sum = 0
    min_number = min_number.to_bytes(4, byteorder='big')
    max_number = max_number.to_bytes(4, byteorder='big')
    #sum = sum.to_bytes(4, byteorder='big')
    count = 0

    with open(filename, "rb") as file:
        while True:
            byte = file.read(4)
            count += 1
            if count % 100000000 == 0:
                print(count)
                print(sum)
            if not byte:
                break
            if byte > max_number:
                max_number = byte
            if byte < min_number:
                min_number = byte
            sum += int.from_bytes(byte, byteorder='big')

    print('max ', int.from_bytes(max_number, byteorder='big'))
    print('min ', int.from_bytes(min_number, byteorder='big'))
    print('sum ', sum)

write_bytes('bytes')
#start_time = time.time()
#read_bytes('bytes')
#print("--- %s seconds ---" % (time.time() - start_time))
