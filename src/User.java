public abstract class User {

	private String id;
	private String username;
	private String name;
	
	public User(String id, String username, String name) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.username = username;
		this.name = name;
	}
	@Override
	public String toString() {
		return username;
	}

}
