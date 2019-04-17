package org.terrence.testapp.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terrence.testapp.domain.Person;
import org.terrence.testapp.repositories.PersonRepository;

@RestController
public class TestRestController {

  @Autowired
  private PersonRepository repo;

  @GetMapping("/test")

  public String runTest() {
    try {
      System.out.println("this is just a test");
      Person test = new Person("TestPerson", 33);
      String id = UUID.randomUUID().toString(); // use a random id
      System.out.println("Using random repo id: " + id);
      test.setId(id);

      // verify there is nothing in the repo with the id and then create the test
      // object
      try {
        repo.findById(id).ifPresent(p -> repo.deleteById(p.getId())); // if there is an existing Person with the id then
                                                                      // delete it
        repo.insert(test);
      } catch (Exception d) {
        System.out.println("exception caught: creating new object");
        repo.insert(test);
      }

      // get the Person by ID and make sure the name and id matche
      Person check = repo.findById(id).get();
      if (((check.getId() == null && test.getId() == null)
          || (check.getId() != null && check.getId().equals(test.getId())))
          && ((check.getName() == null && test.getName() == null)
              || (check.getName() != null && check.getName().equals(test.getName())))) {
        repo.deleteById(id);
        return "test passed: objects matched!";
      } else {
        repo.deleteById(id);
        return "test failed: ojects do not match";
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e);
    }
    return "done";
  }
}