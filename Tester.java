import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Tester {
	BinomialHeap generatedHeap;
	List<Integer> list; 
	int numberOfDeletedElements=0;
	int numberOfLinksWhileInserting=0;
	int numberOfLinksWhileDeleting=0;

	public void sortList(List<Integer> list){
		Collections.sort(list, new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
		});
	}
	public boolean testMin(){
		if(list.isEmpty()&&list.get(0)!=this.generatedHeap.findMin().key)
			return false;
		return true;
	}
	public boolean testSize(){
		if(this.generatedHeap.size()==this.list.size())
			return true;
		return false;
	}
	public boolean testIsEmpty(){
		if(this.generatedHeap.empty())
			return true;
		return false;
	}
	public boolean testDelete(){
		try{
			this.generatedHeap.deleteMin();
			numberOfDeletedElements++;
                        SanitizeBinomialHeap.sanitize(this.generatedHeap);
		}catch(Exception e){
		        System.out.println(e.getMessage());
			return false;
		}
		int elem=this.list.get(0);
		this.list.remove(0);
		if(!this.list.isEmpty()&&this.list.get(0)!=elem){
			if(this.generatedHeap.findMin().key==elem)
				return false;
		}
		return true;
	}
//	public void testArrayToHeap(int maxValue, int size){
//		System.out.println("Creating new heap from an array and testing all methods again...");
//		Random rand=new Random();
//		int[] arr=new int[size];
//		int random;
//		this.list=new ArrayList<>();
//		for(int i=0;i<size;i++){
//			random=rand.nextInt(maxValue);
//			this.list.add(random);
//			arr[i]=random;
//		}
//		try{
//		this.generatedHeap.arrayToHeap(arr);
//		}catch(Exception e){
//			System.err.println("Problem with ArrayToHeap method... :/");
//		}
//		sortList(this.list);
//		System.out.println("ArrayToHeap works.. testing all methods again!");
//		runTest();
//	}
	public static boolean xor(boolean x, boolean y) {
		return ((x || y) && !(x && y));
	}
//	public boolean testBinaryRep(){
//		String binaryRep=Integer.toBinaryString(this.list.size());
//		boolean[] heapBinaryRep;
//		int cnt=0;
//		try{
//			heapBinaryRep=this.generatedHeap.binaryRep();
//		}catch(Exception e){
//			return false;
//		}
//		char bit;
//		for(boolean bool:heapBinaryRep){
//			bit=bool ? '1' : '0';
//			if(bit!=binaryRep.charAt(binaryRep.length()-1-cnt))
//				return false;
//			cnt++;
//		}
//		return true;
//	}
//	
	public void runTest(){
//		this.numberOfLinksWhileInserting=this.generatedHeap.numberOfLinks;
//		this.generatedHeap.numberOfLinks=0;
//		System.out.println("Start deleting elements and testing other methods...");
		while(this.list.size()!=0){
//			if(!this.testBinaryRep()){
//				System.err.println("Error with BinaryRep method!");
//				System.exit(1);
//			}
			if(!this.testMin()){
				System.err.println("Error with findMin method after deleting the "+numberOfDeletedElements+" element!");
				System.exit(1);
			}if(!this.testDelete()){
				System.err.println("Error with deleteMin method while trying to delete the "+(numberOfDeletedElements+1)+" element!");
				System.exit(1);

			}if(!this.testSize()){
				System.err.println("Error with size method!");
				System.exit(1);
			}
		}
		if(!this.testIsEmpty()){
			System.err.println("Problem with IsEmpty method... :/");
		}else{
//			System.out.println("IsEmpty method works!");
		}
//		System.out.println("BinaryRep method works...");
//		System.out.println("findMin method works...");
//		System.out.println("deleteMin method works...");
//		System.out.println("size method works...");
//		this.numberOfLinksWhileDeleting=this.generatedHeap.numberOfLinks;
//		this.generatedHeap.numberOfLinks=0;
	}
	
	public void mixedTest(){
		Random rand=new Random();
		int random;
		for(int i=1;i<=20000;i++){
			random=rand.nextInt(Integer.MAX_VALUE);
			this.generatedHeap.insert(random, Integer.toString(random));
			this.list.add(random);
                        SanitizeBinomialHeap.sanitize(this.generatedHeap);
                        
			if(list.size()>1&&rand.nextGaussian()<0.35){
//				this.numberOfLinksWhileInserting+=this.generatedHeap.numberOfLinks;
//				this.generatedHeap.numberOfLinks=0;
				sortList(list);
				if(!this.testDelete())
					System.err.println("Error with deleteMin method while trying to delete the "+(numberOfDeletedElements+1)+" element!");
//				this.numberOfLinksWhileDeleting+=this.generatedHeap.numberOfLinks;
//				this.generatedHeap.numberOfLinks=0;
				if(!this.testMin())
					System.err.println("Error with findMin method after deleting the "+numberOfDeletedElements+" element!");
//				if(!this.testBinaryRep())
//					System.err.println("Error with BinaryRep method!");
				if(!this.testSize())
					System.err.println("Error with size method!");
			}
		}
		sortList(list);
//		this.numberOfLinksWhileInserting+=this.generatedHeap.numberOfLinks;
//		this.generatedHeap.numberOfLinks=0;
		while(list.size()-1!=0){
			if(!this.testDelete())
				System.err.println("Error with deleteMin method while trying to delete the "+(numberOfDeletedElements+1)+" element!");

			if(!this.testMin())
				System.err.println("Error with findMin method after deleting the "+numberOfDeletedElements+" element!");
//			if(!this.testBinaryRep())
//				System.err.println("Error with BinaryRep method!");
			if(!this.testSize())
				System.err.println("Error with size method!");
		}
//		this.numberOfLinksWhileDeleting+=this.generatedHeap.numberOfLinks;
		System.out.println("Success! :)");
	}
	public void run(){
		DecimalFormat df=new DecimalFormat();
		df.setMaximumFractionDigits(2);
		for(int i=1;i<=10;i++){
			System.out.println("###########Test "+i+"###########");
			initialize();
//			System.out.println("Inserting items...");
			generateHeap(i*300000, i*1000,this.generatedHeap, this.list);
			System.out.println(i*1000+" elements were inserted to the heap. running tests...");
			runTest();
//			System.out.println("Number of linkings after (all) insertions: "+this.numberOfLinksWhileInserting);
//			System.out.println("Average number of linking operations: "+df.format((double) this.numberOfLinksWhileInserting/(i*1000)));
//			System.out.println("Number of linkings after (all) deletions: "+this.numberOfLinksWhileDeleting);
//			System.out.println("Average number of linking operations: "+df.format((double) this.numberOfLinksWhileDeleting/(i*1000)));
//			System.out.println("Total linking operations: "+(this.numberOfLinksWhileDeleting+this.numberOfLinksWhileInserting));
//			testArrayToHeap(i*300000, i*500);
			initialize();
			testMeld(i*300000, i*1000);
			initialize();
		}
		System.out.println("###########Test "+11+" Mixed Test###########");
		mixedTest();
//		System.out.println("Number of linkings after (all) insertions: "+this.numberOfLinksWhileInserting);
//		System.out.println("Average number of linking operations: "+df.format((double) this.numberOfLinksWhileInserting/20000));
//		System.out.println("Number of linkings after (all) deletions: "+this.numberOfLinksWhileDeleting);
//		System.out.println("Average number of linking operations: "+df.format((double) this.numberOfLinksWhileDeleting/20000));
//		System.out.println("Total linking operations: "+(this.numberOfLinksWhileDeleting+this.numberOfLinksWhileInserting));
		initialize();
	}
	
	public void initialize(){
		this.generatedHeap=new BinomialHeap();
		this.list=new ArrayList<>();
		this.numberOfDeletedElements=0;
		this.numberOfLinksWhileInserting=0;
		this.numberOfLinksWhileDeleting=0;
	}
	
	public void generateHeap(int maxValue, int size, BinomialHeap heap, List<Integer> list){
		Random rand=new Random();
		int random;
		try{
			for(int i=0;i<size;i++){
				do{
					random=rand.nextInt(maxValue);
				}while(list.contains(random));
				heap.insert(random, Integer.toString(random));
				list.add(random);
			}
		}catch(Exception e){
			throw new RuntimeException("problem with insert/meld methods! :/");
		}
		sortList(list);
	}	
	public void testMeld(int maxValue, int size){
//		System.out.println("Testing melding 2 heaps: ");
		BinomialHeap heap=new BinomialHeap();
		List<Integer> list=new ArrayList<>();
		generateHeap(maxValue, size, heap, list);
		generateHeap(maxValue, size, this.generatedHeap, this.list);
		SanitizeBinomialHeap.sanitize(heap);
		try{
			this.generatedHeap.meld(heap);
			SanitizeBinomialHeap.sanitize(this.generatedHeap);
		}catch(Exception e){
			throw new RuntimeException("Error melding 2 heaps.. :/");
		}
		this.list.addAll(list);
		this.sortList(this.list);
//		System.out.println("2 heaps were melded!\nThe new (melded) heap has "+2*size+" elements");
		runTest();
		System.out.println("Success! :)");
	}
}