# UI Implementation Plan for Cargo Management System Using Java Swing

## 1. UI Components Breakdown

**Frames/Dialogs:**
- LoginFrame
- RegistrationFrame
- MainDashboardFrame
- AddCargoFrame
- SearchCargoFrame
- CargoListFrame
- CityTreeFrame
- RouteViewFrame

**Common Components:**
- JTextField
- JPasswordField
- JButton
- JComboBox
- JTable
- JTree
- JScrollPane
- JLabel
- JOptionPane

**Layout Managers:**
- `CardLayout` for dynamic panel switching in the main dashboard.
- `GridBagLayout` / `BorderLayout` for form alignment.
- `GroupLayout` for complex forms.

## 2. Screen Designs

### 2.1 Authentication

**Registration Screen:**
- **Fields:**
    - First Name (text input)
    - Last Name (text input)
    - User ID (text input)
- **Button:** "Register" (validates uniqueness via backend `HashMap`).
- **Success:** Show `JOptionPane` with generated User ID; redirect to login.
- **Error:** Display "User ID already exists" dialog.

**Login Screen:**
- **Field:** User ID (text input).
- **Button:** "Login" (validates against backend `HashMap`).
- **Error:** "Invalid User ID" dialog on failure.

### 2.2 Main Dashboard

**Components:**
- Menu bar with options: "Add Cargo," "View History," "Search," etc.
- Grid of buttons for quick actions (e.g., "Add Cargo," "Process Priority").
- Logout button (returns to login screen).

### 2.3 Add Cargo Screen

**Form:**
- Cargo ID (text field).
- Date (text field with date picker or validation).
- Status (dropdown: Processed/On Delivery/Delivered).
- City ID (text field).
- **Submit Button:**
    - Calls backend `addCargo()` method.
    - Displays estimated delivery time (from tree depth calculation) in a dialog.

### 2.4 Cargo List Screens

**Last 5 Shipments:**
- `JTable` with columns: Cargo ID, Date, Status, City.
- Data fetched from backend `Stack`.

**Delivered/Undelivered Lists:**
- Filtered tables using backend `Timsort` / `Merge Sort`.

### 2.5 Search Cargo Screen

- **Search Bar:** Input for Cargo ID.
- **Results Panel:** Displays cargo details (status, city, delivery time) in a text area.

### 2.6 City Tree and Route Visualization

**Tree Structure:**
- `JTree` component populated from backend tree data.
- Root: Central Warehouse; Children: Cities.

**Route Display:**
- Textual path (e.g., "Warehouse → City A → City B") using depth-first traversal.

### 2.7 Priority Cargo Processing

**Panel:**
- Displays next priority cargo (from `PriorityQueue`).
- "Process Now" button removes it from the queue and updates the UI.

## 3. Navigation Flow

**Start:** `LoginFrame` → (Successful login) → `MainDashboardFrame`.
**Registration:** `LoginFrame` → `RegistrationFrame` → (Back to Login).

**Main Dashboard Actions:**
- "Add Cargo" → `AddCargoFrame`.
- "View Last 5" → `CargoListFrame` (filtered to last 5).
- "Search" → `SearchCargoFrame`.
- "View City Tree" → `CityTreeFrame`.

**Return:** All screens include "Back to Dashboard" buttons.

## 4. Backend Integration

**Data Binding:**
- `AddCargoFrame` calls `CargoManager.addCargo(...)`.
- `SearchCargoFrame` uses `CargoManager.searchCargo(id)`.
- Priority processing uses `PriorityQueue.poll()`.

**Tree Depth Calculation:**
- `CityTree.getDepth(cityId)` for delivery time estimation.

**Sorting:**
- `MergeSort.sort(deliveredCargoList)` for status-based lists.

## 5. Implementation Steps

1. **Setup Project:**
    - Import existing backend code into a new Java Swing project.
2. **Create Authentication Screens:**
    - Implement `LoginFrame` and `RegistrationFrame` with input validation.
3. **Build Main Dashboard:**
    - Use `CardLayout` to manage feature panels.
4. **Develop Feature Screens:**
    - `AddCargoFrame` (form submission).
    - `CargoListFrame` (tabular data with `JTable`).
    - `CityTreeFrame` (visualize with `JTree`).
5. **Integrate Backend Methods:**
    - Connect UI actions to backend logic (e.g., adding cargo updates the `LinkedList`).
6. **Test Navigation and Data Flow:**
    - Ensure all screens update correctly and handle edge cases (e.g., empty lists).
7. **Polish UI:**
    - Apply consistent styling (colors, fonts) and error handling.

## 6. Potential Challenges & Solutions

**Challenge 1:** Real-time updates for cargo lists.
**Solution:** Use `TableModel` listeners or refresh data on panel visibility.

**Challenge 2:** Visualizing city tree hierarchy.
**Solution:** Map backend tree nodes to `DefaultMutableTreeNode` for `JTree`.

**Challenge 3:** Thread blocking during backend operations.
**Solution:** Run backend calls in `SwingWorker` threads to prevent UI freeze.

**Final Note:** Prioritize building a modular UI with clear separation between components (e.g., each feature in its own class)