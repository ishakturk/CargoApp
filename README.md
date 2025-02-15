# model.Cargo Management System

## Overview
This application is a console-based cargo management system developed in Java. It utilizes various data structures like Linked List, Priority Queue, Tree, Stack, and HashMap to efficiently manage cargo operations. The system is compiled using **JDK 23**, and the executable JAR file is named **Kargo.jar**.

## Features
- **Customer Data Management** using Linked List
- **model.Cargo Prioritization** with Priority Queue
- **Delivery Route Management** using Tree Data Structure
- **Shipping History Tracking** with Stack
- **Sorting model.Cargo model.Status** using Timsort and Merge Sort
- **Delivery Time Estimation** based on Tree Depth
- **Unique User ID Assignment** with HashMap

## Requirements
- **Java Development Kit (JDK 23)** must be installed on your system.

## Installation
### Step 1: Install JDK 23
1. Download **JDK 23** from the official website.
2. Install JDK 23 on your computer.
3. Verify installation using the command:
   ```sh
   java -version
   ```

### Step 2: Run the JAR File
1. Open the **command prompt** (Windows: `cmd`, Mac/Linux: `Terminal`).
2. Navigate to the directory where **Kargo.jar** is located:
   ```sh
   cd Kargo/out/artifacts/Kargo_jar/Kargo.jar
   ```
3. Run the JAR file using:
   ```sh
   java -jar Kargo.jar
   ```

## System Flow
### User Authentication
1. **Register:**
   - Enter **First Name, Last Name, and a Unique User ID**.
   - After successful registration, a welcome message with your **User ID** is displayed.
2. **Login:**
   - Enter your **User ID** to access the system.
   - If the User ID is incorrect, an error message is displayed.
3. **Logout:**
   - Exit the system using the **Logout** option.

### Main Menu Options
1. **Add model.Cargo:**
   - Input **model.Cargo ID, Date, model.Status (Processed, On Delivery, Delivered), and City ID**.
   - The system calculates **delivery time** based on city depth in the tree structure.
2. **List My Last 5 model.Cargo Shipments:**
3. **Search model.Cargo by ID:**
4. **List Delivered model.Cargo:**
5. **List Undelivered model.Cargo:**
6. **Print model.Cargo Routes:**
7. **Process Priority model.Cargo:**
8. **Print City Tree Structure:**
9. **Exit System**

## Data Structures Used
### 1. **Linked List** (Customer Data Management)
- Efficient for **dynamic memory allocation** and **fast insert/delete operations**.
- Alternative: `ArrayList` or `HashMap`.

### 2. **Priority Queue** (model.Cargo Prioritization)
- Helps sort **urgent deliveries**.
- Alternative: `Heap` (Min-Heap for fast sorting by priority).

### 3. **Tree Data Structure** (model.Cargo Routing)
- The root node represents the **central warehouse**, while child nodes represent **delivery cities**.
- Used for **hierarchical delivery tracking and shortest route calculations**.

### 4. **Stack** (Shipping History Tracking)
- **LIFO (Last In, First Out)** principle helps in quick retrieval of recent shipments.
- Alternative: `LinkedList` for managing the last 5 records dynamically.

### 5. **Sorting Algorithms**
- **Timsort**: Java's default sorting algorithm, optimized for real-world data.
- **Merge Sort**: Used for handling large data sets efficiently.

### 6. **HashMap** (Unique User ID Management)
- Ensures each user has a **unique identifier** and allows quick lookup.

## Notes
- The system ensures accurate **delivery time estimation** by considering tree depth.
- Priority Queue and Heap structures are used for **optimized cargo prioritization**.
- The city tree structure maintains **delivery dependencies** and **shortest path calculations**.

---
**Developed with Java | JDK 23 | Data Structures & Algorithms**
