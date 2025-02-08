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
}