# UI Implementation Plan for model.Cargo Management System Using Java Swing

## 1. UI Components Breakdown

**Frames/Dialogs:**
- ui.LoginFrame
- ui.RegistrationFrame
- ui.MainDashboardFrame
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
- Menu bar with options: "Add model.Cargo," "View History," "Search," etc.
- Grid of buttons for quick actions (e.g., "Add model.Cargo," "Process Priority").
- Logout button (returns to login screen).

### 2.3 Add model.Cargo Screen

**Form:**
- model.Cargo ID (text field).
- Date (text field with date picker or validation).
- model.Status (dropdown: Processed/On Delivery/Delivered).
- City ID (text field).
- **Submit Button:**
    - Calls backend `addCargo()` method.
    - Displays estimated delivery time (from tree depth calculation) in a dialog.

### 2.4 model.Cargo List Screens

**Last 5 Shipments:**
- `JTable` with columns: model.Cargo ID, Date, model.Status, City.
- Data fetched from backend `Stack`.

**Delivered/Undelivered Lists:**
- Filtered tables using backend `Timsort` / `Merge Sort`.

### 2.5 Search model.Cargo Screen

- **Search Bar:** Input for model.Cargo ID.
- **Results Panel:** Displays cargo details (status, city, delivery time) in a text area.

### 2.6 City Tree and Route Visualization

**Tree Structure:**
- `JTree` component populated from backend tree data.
- Root: Central Warehouse; Children: Cities.

**Route Display:**
- Textual path (e.g., "Warehouse → City A → City B") using depth-first traversal.

### 2.7 Priority model.Cargo Processing

**Panel:**
- Displays next priority cargo (from `PriorityQueue`).
- "Process Now" button removes it from the queue and updates the UI.

## 3. Navigation Flow

**Start:** `ui.LoginFrame` → (Successful login) → `ui.MainDashboardFrame`.
**Registration:** `ui.LoginFrame` → `ui.RegistrationFrame` → (Back to Login).

**Main Dashboard Actions:**
- "Add model.Cargo" → `AddCargoFrame`.
- "View Last 5" → `CargoListFrame` (filtered to last 5).
- "Search" → `SearchCargoFrame`.
- "View City Tree" → `CityTreeFrame`.

**Return:** All screens include "Back to Dashboard" buttons.

## 4. Backend Integration

**Data Binding:**
- `AddCargoFrame` calls `service.CargoManager.addCargo(...)`.
- `SearchCargoFrame` uses `service.CargoManager.searchCargo(id)`.
- Priority processing uses `PriorityQueue.poll()`.

**Tree Depth Calculation:**
- `model.CityTree.getDepth(cityId)` for delivery time estimation.

**Sorting:**
- `MergeSort.sort(deliveredCargoList)` for status-based lists.

## 5. Implementation Steps

1. **Setup Project:**
    - Import existing backend code into a new Java Swing project.
2. **Create Authentication Screens:**
    - Implement `ui.LoginFrame` and `ui.RegistrationFrame` with input validation.
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