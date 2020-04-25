package model;

/**
 * The Subject Class is a container for all information
 * associated with a Subject. This is used by both the
 * Client and Server side as a compact way of sending
 * and receiving all information on a specific subject.
 * <p>
 *   The Subject Class is usually contained within a
 *   SubjectManager.
 * </p>
 * <p>
 *   The information on the Subject is obtained from
 *   the Server, specifically the MySQL database that
 *   contains all information on the subjects.
 * </p>
 *
 * @author James Gardner
 */
public class Subject {
  private int id;
  private String name;
  private String category;

  /**
   * CONSTRUCTOR DESCRIPTION.
   *
   * @param id SQL row number in subjects table
   * @param name String mame of the subject
   * @param category
   * .
   */
  public Subject(int id, String name, String category) {
    this.id = id;
    this.name = name;
    this.category = category;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
