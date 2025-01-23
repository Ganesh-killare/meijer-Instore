package demo;

class parent{
	
	void xyz() {
		System.out.println("this is parent");
	}
	
	void abc() {
		System.out.println("this is child abc");
	}
}

class child extends parent{
	
	@Override
	void xyz() {
		System.out.println("this is child");
	}
}

public class sampleChild {
	
	public static void main(String[] args) {
		parent p = new child();
		p.xyz();
		p.abc();
	
	}
}
