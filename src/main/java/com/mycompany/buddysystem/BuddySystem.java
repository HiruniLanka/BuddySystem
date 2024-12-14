package com.mycompany.buddysystem;

import java.util.ArrayList;

class BuddySystem {
    private static final int TOTAL_MEMORY = 1024; // Total memory available (in KB)
    private ArrayList<Integer> memory = new ArrayList<>(); // Memory blocks list

    // Constructor: Initializes the memory system
    public BuddySystem() {
        memory.add(TOTAL_MEMORY); // Start with a single free block of 1024KB
    }

    // Method to allocate memory
    public void allocate(int requestSize) {
        int blockSize = getNextPowerOfTwo(requestSize); // Find the nearest power of 2 for the requested size
        int index = findFreeBlock(blockSize); // Find an available free block of the required size

        if (index == -1) { // If no suitable block is found
            System.out.println("Allocation failed for " + requestSize + "KB. Not enough memory.");
            return;
        }

        // Split the block into smaller sizes until it matches the requested size
        while (memory.get(index) > blockSize) {
            int currentSize = memory.get(index); // Get the current block size
            memory.set(index, currentSize / 2); // Reduce the size of the block
            memory.add(index + 1, currentSize / 2); // Add the second half of the block to the memory list
        }

        memory.set(index, -blockSize); // Mark the block as allocated (negative value indicates allocation)
        System.out.println("Allocated " + blockSize + "KB.");
    }

    // Method to free allocated memory
    public void free(int blockSize) {
        int index = memory.indexOf(-blockSize); // Find the index of the allocated block

        if (index == -1) { // If the block is not found
            System.out.println("Free failed for " + blockSize + "KB. Block not found.");
            return;
        }

        memory.set(index, blockSize); // Mark the block as free (positive value indicates it is free)
        System.out.println("Freed " + blockSize + "KB.");
        merge(); // Attempt to merge adjacent free blocks
    }

    // Method to merge adjacent free memory blocks
    private void merge() {
        for (int i = 0; i < memory.size() - 1; i++) {
            // Check if the current block and the next block are both free and of the same size
            if (memory.get(i) > 0 && memory.get(i).equals(memory.get(i + 1))) {
                memory.set(i, memory.get(i) * 2); // Merge the two blocks into a larger block
                memory.remove(i + 1); // Remove the second block from the list
                i--; // Re-check the current index for further merges
            }
        }
    }

    // Method to find a free memory block of sufficient size
    private int findFreeBlock(int blockSize) {
        for (int i = 0; i < memory.size(); i++) {
            if (memory.get(i) >= blockSize) { // Check if the block size is sufficient
                return i; // Return the index of the free block
            }
        }
        return -1; // Return -1 if no suitable block is found
    }

    // Helper method to calculate the next power of two for a given size
    private int getNextPowerOfTwo(int size) {
        int power = 1; // Start with 1
        while (power < size) { // Double the power until it is greater than or equal to the requested size
            power *= 2;
        }
        return power;
    }

    // Method to display the current state of memory
    public void displayMemory() {
        System.out.println("Current Memory State:");
        for (int block : memory) { // Iterate over all blocks in the memory
            if (block > 0) { // Positive values indicate free blocks
                System.out.println(block + "KB (free)");
            } else { // Negative values indicate allocated blocks
                System.out.println(-block + "KB (allocated)");
            }
        }
    }

    // Main method to test the Buddy System
    public static void main(String[] args) {
        BuddySystem buddySystem = new BuddySystem(); // Create an instance of the BuddySystem

        buddySystem.displayMemory(); // Display the initial memory state

        System.out.println("\nStep 1: Allocate 100KB");
        buddySystem.allocate(100); // Allocate 100KB
        buddySystem.displayMemory(); // Display the memory state after allocation

        System.out.println("\nStep 2: Allocate 200KB");
        buddySystem.allocate(200); // Allocate 200KB
        buddySystem.displayMemory(); // Display the memory state after allocation

        System.out.println("\nStep 3: Free 128KB");
        buddySystem.free(128); // Free 128KB block
        buddySystem.displayMemory(); // Display the memory state after freeing the block

        System.out.println("\nStep 4: Free 256KB");
        buddySystem.free(256); // Free 256KB block
        buddySystem.displayMemory(); // Display the memory state after freeing the block
    }
}
