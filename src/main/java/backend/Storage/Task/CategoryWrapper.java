package main.java.backend.Storage.Task;

public class CategoryWrapper {
	
	private Category category;
	private String categoryName;
	
	public CategoryWrapper() {
		
	}
	
	public CategoryWrapper(Category category, String categoryName) {
		setCategory(category);
		setCategoryName(categoryName);
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
