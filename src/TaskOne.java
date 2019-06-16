import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class TaskOne {
	
	static volatile long min = 1233172983719823792L;
	static volatile long max = 0L;
	static volatile BigInteger sum = BigInteger.valueOf(0L);
	static long tempInt;
	
	static long getUnsignedInt(int x) {
		return x & 0x00000000ffffffffL;
	}
	
	static void minMaxSum(int temp){
		tempInt = getUnsignedInt(temp);
		if(tempInt > max){max = tempInt;}
		if(tempInt < min){min = tempInt;}
		sum = sum.add(BigInteger.valueOf(tempInt));
	}
	
	private static void printReset(){
		System.out.println("Min : " + min);
		System.out.println("Max : " + max);
		System.out.println("Sum : " + sum);
		min = 1233172983719823792L;
		max = 0L;
		sum = BigInteger.valueOf(0);
	}
	
	private static void parallel() throws IOException {
		try {
			
			String filename = "C:\\Users\\Alex\\PythonProjects\\BIgData\\bytes";
			long size = new RandomAccessFile(filename, "r").length();
			size =  size/4;
			
			Thread t1 = new Thread(new SearchThread(filename, 0, (int) size));
			Thread t2 = new Thread(new SearchThread(filename, (int) size, (int) size));
			Thread t3 = new Thread(new SearchThread(filename, (int) size*2, (int) size));
			Thread t4 = new Thread(new SearchThread(filename, (int) size*3, (int) size));
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			
			while(t1.isAlive()||t2.isAlive()||t4.isAlive()||t3.isAlive()){
				continue;
			}
		}
		finally {
			printReset();
		}
	}
	
	private static void simple() throws IOException {
		FileInputStream fileInputStream = null;
		DataInputStream data;
		BufferedInputStream bufferedInputStream;
		
		try {
			fileInputStream = new FileInputStream("C:\\Users\\Alex\\PythonProjects\\BIgData\\bytes");
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			data = new DataInputStream(bufferedInputStream);
			while (true){
				minMaxSum(data.readInt());
			}
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			printReset();
		}
	}
	
	public static void main(String[] args) {
		long time = System.nanoTime();
		try{simple();}
		catch (IOException e){}
		time = System.nanoTime() - time;
		System.out.println(TimeUnit.SECONDS.convert(time, TimeUnit.NANOSECONDS)
				+ " seconds");
		
		time = System.nanoTime();
		try{parallel();}
		catch (IOException e){}
		time = System.nanoTime() - time;
		System.out.println(TimeUnit.SECONDS.convert(time, TimeUnit.NANOSECONDS)
				+ " seconds");
	}
}

class SearchThread implements Runnable{
	private long min = 1233172983719823792L;
	private long max = 0L;
	private BigInteger sum = BigInteger.valueOf(0L);
	private int position;
	private int size;
	private String filename;
	private long tempInt;
	
	SearchThread(String filename, int position, int size){
		this.position = position;
		this.size = size;
		this.filename = filename;
	}
	public void run(){
		try{
			RandomAccessFile file = new RandomAccessFile(filename, "r");
			FileChannel channel = file.getChannel();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, size);
			while (buffer.hasRemaining()){
				tempInt = TaskOne.getUnsignedInt(buffer.getInt());
				if(tempInt > max){max = tempInt;}
				if(tempInt < min){min = tempInt;}
				sum = sum.add(BigInteger.valueOf(tempInt));
			}
			
			update();
			
			buffer.clear();
			channel.close();
			file.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void update(){
		if(max > TaskOne.max){TaskOne.max = max;}
		if(min < TaskOne.min){TaskOne.min = min;}
		TaskOne.sum = TaskOne.sum.add(sum);
	}
}