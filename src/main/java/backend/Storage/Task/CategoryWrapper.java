package main.java.backend.Storage.Task;

public class CategoryWrapper implements Comparable<CategoryWrapper> {
	
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
	
	@Override
	public int compareTo(CategoryWrapper o) {
		
		if(this.categoryName.compareTo(o.categoryName) < 0) {
			return -1;
		} else if(this.categoryName.compareTo(o.categoryName) > 0) {
			return 1;
		} else {
			return 0;
		}
	}
}
