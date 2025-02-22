package dev.kanchanop.handytools;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HandytoolsControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetAllTools() {
        ResponseEntity<Storage[]> response = restTemplate.getForEntity("/handytools", Storage[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetOneTool() {
        // First create a tool
        Storage newTool = new Storage("Test Tool", "Test Owner", "Test Location", false, "");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();
        assertThat(toolId).isNotNull();
        
        // Then get the tool
        ResponseEntity<Storage> response = restTemplate.getForEntity("/handytools/" + toolId, Storage.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage retrievedTool = response.getBody();
        assertThat(retrievedTool).isNotNull();
        assertThat(retrievedTool.getToolDetail()).isEqualTo("Test Tool");
    }

    @Test
    void shouldCreateNewTool() {
        Storage newTool = new Storage("Saw", "Mike", "Workshop", false, "");
        ResponseEntity<Storage> response = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage createdTool = response.getBody();
        assertThat(createdTool).isNotNull();
        assertThat(createdTool.getToolDetail()).isEqualTo("Saw");
        assertThat(createdTool.getOwnerName()).isEqualTo("Mike");
    }

    @Test
    void shouldUpdateTool() {
        // First create a tool
        Storage newTool = new Storage("Original Tool", "Original Owner", "Original Location", false, "");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();
        assertThat(toolId).isNotNull();

        // Then update it
        Storage updatedTool = new Storage("Updated Tool", "New Owner", "New Location", true, "Borrower");
        ResponseEntity<Storage> response = restTemplate.exchange(
            "/handytools/" + toolId,
            HttpMethod.PUT,
            new HttpEntity<>(updatedTool),
            Storage.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage resultTool = response.getBody();
        assertThat(resultTool).isNotNull();
        assertThat(resultTool.getToolDetail()).isEqualTo("Updated Tool");
        assertThat(resultTool.isBorrowed()).isTrue();
    }

    @Test
    void shouldDeleteTool() {
        // First create a tool
        Storage newTool = new Storage("Tool to Delete", "Owner", "Location", false, "");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();
        assertThat(toolId).isNotNull();

        // Then delete it
        restTemplate.delete("/handytools/" + toolId);

        // Verify it's gone
        ResponseEntity<Storage> response = restTemplate.getForEntity("/handytools/" + toolId, Storage.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturn404WhenToolNotFound() {
        ResponseEntity<Storage> response = restTemplate.getForEntity("/handytools/999", Storage.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldBorrowTool() {
        // Create a new tool
        Storage newTool = new Storage("Test Tool", "Test Owner", "Test Location", false, "");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();

        // Borrow the tool
        String borrower = "Test Borrower";
        ResponseEntity<Storage> borrowResponse = restTemplate.exchange(
            "/handytools/" + toolId + "/borrow",
            HttpMethod.PUT,
            new HttpEntity<>(borrower),
            Storage.class
        );

        assertThat(borrowResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage borrowedTool = borrowResponse.getBody();
        assertThat(borrowedTool).isNotNull();
        assertThat(borrowedTool.isBorrowed()).isTrue();
        assertThat(borrowedTool.getBorrowerName()).isEqualTo(borrower);
    }

    @Test
    void shouldReturnTool() {
        // Create a borrowed tool
        Storage newTool = new Storage("Test Tool", "Test Owner", "Test Location", true, "Current Borrower");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();

        // Return the tool
        ResponseEntity<Storage> returnResponse = restTemplate.exchange(
            "/handytools/" + toolId + "/return",
            HttpMethod.PUT,
            HttpEntity.EMPTY,
            Storage.class
        );

        assertThat(returnResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage returnedTool = returnResponse.getBody();
        assertThat(returnedTool).isNotNull();
        assertThat(returnedTool.isBorrowed()).isFalse();
        assertThat(returnedTool.getBorrowerName()).isEmpty();
    }

    @Test
    void shouldNotBorrowAlreadyBorrowedTool() {
        // Create an already borrowed tool
        Storage newTool = new Storage("Test Tool", "Test Owner", "Test Location", true, "Current Borrower");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", newTool, Storage.class);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();

        // Try to borrow it again
        String newBorrower = "New Borrower";
        ResponseEntity<String> borrowResponse = restTemplate.exchange(
            "/handytools/" + toolId + "/borrow",
            HttpMethod.PUT,
            new HttpEntity<>(newBorrower),
            String.class
        );

        // Should return 409 CONFLICT with appropriate error message
        assertThat(borrowResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(borrowResponse.getBody()).contains("Conflict");
    }

    @Test
    void shouldFindAvailableTools() {
        ResponseEntity<Storage[]> response = restTemplate.getForEntity("/handytools/available", Storage[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage[] tools = response.getBody();
        assertThat(tools).isNotNull();
        assertThat(tools).allMatch(tool -> !tool.isBorrowed());
    }

    @Test
    void shouldFindBorrowedTools() {
        ResponseEntity<Storage[]> response = restTemplate.getForEntity("/handytools/borrowed", Storage[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage[] tools = response.getBody();
        assertThat(tools).isNotNull();
        assertThat(tools).allMatch(Storage::isBorrowed);
    }

    @Test
    void shouldFindToolsBorrowedByUser() {
        // Create a tool that's borrowed by "TestUser"
        Storage borrowedTool = new Storage("Test Tool", "Owner", "Location", true, "TestUser");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", borrowedTool, Storage.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Create another tool that's borrowed by someone else
        Storage otherTool = new Storage("Other Tool", "Owner", "Location", true, "OtherUser");
        restTemplate.postForEntity("/handytools", otherTool, Storage.class);
        
        // Test finding tools borrowed by TestUser
        ResponseEntity<Storage[]> response = restTemplate.getForEntity("/handytools/borrowed-by/TestUser", Storage[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage[] tools = response.getBody();
        assertThat(tools).isNotNull();
        assertThat(tools).hasSize(1);
        assertThat(tools[0].getBorrowerName()).isEqualTo("TestUser");
    }

    @Test
    void shouldHandleConflictWhenReturningNonBorrowedTool() {
        // Create a non-borrowed tool
        Storage tool = new Storage("Test Tool", "Owner", "Location", false, "");
        ResponseEntity<Storage> createResponse = restTemplate.postForEntity("/handytools", tool, Storage.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Storage createdTool = createResponse.getBody();
        assertThat(createdTool).isNotNull();
        Long toolId = createdTool.getId();

        // Try to return it
        ResponseEntity<String> response = restTemplate.exchange(
            "/handytools/" + toolId + "/return",
            HttpMethod.PUT,
            HttpEntity.EMPTY,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}