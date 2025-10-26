# 🌹 Rose Ledger

**Rose Ledger** is a simple, modern, and insightful expense tracker designed to provide a clear overview of your spending habits.  
Built entirely with **modern Android development tools** and **Jetpack Compose**.

---

## ✨ Features

Rose Ledger is a feature-rich application that helps you manage your finances effectively.

### **Dashboard (Home)**
Get an at-a-glance summary of your spending for the current month:
- Total expenses
- Transaction count
- Top spending category
- Average daily spend

### **Expense Tracking**
A comprehensive list of all your transactions.
- Add, Edit, and Delete expenses with ease  
- Archive System: Keep your main list clean by archiving old transactions (viewable in the “Archived” tab)  
- Search: Quickly find transactions by description or category  

### **Financial Reports**
Analyze your spending deeply and visually.
- Dynamic Line Chart: Visualize trends over different periods  
- Filter by Time: View data by **Day**, **Week**, **Month**, or **Year**  
- Category Breakdown: Visual spending breakdown with percentage bars  

### **Settings**
Customize the app to your liking.
- Category Management: Add, view, and delete custom expense categories  
- Theme Customization: Choose between **Light**, **Dark**, or **System Default** themes  

### **About Screen**
Includes app information, credits, and technologies used to build the project.

---

## 💡 Credits & Concept

The concept and idea for Rose Ledger were created by **Eugine Jose** & **Sherry Rose Pajares**.

---

## 🛠️ Built With

This project showcases modern Android development practices and leverages a suite of powerful tools from the Android ecosystem.

### **Core Technologies**
- **Kotlin** – Primary language for concise, expressive, and safe Android development  
- **Jetpack Compose** – Declarative UI framework for modern Android apps  
- **Material 3** – Google's latest design system for consistent, beautiful UI  
- **MVVM Architecture** – For clean, scalable, and maintainable structure  

---

## 📚 Libraries & Components

*(Based on the project’s `libs.versions.toml`)*

### **UI & Navigation**
- `androidx.activity:activity-compose` – Integrates Compose into activities  
- `androidx.navigation:navigation-compose` – Handles in-app navigation  
- `androidx.compose.material3:material3-adaptive-navigation-suite` – Provides adaptive navigation UI  

### **Data & State Management**
- `androidx.lifecycle:lifecycle-runtime-ktx` – Lifecycle-aware components  
- `androidx.lifecycle:lifecycle-viewmodel-compose` – ViewModel integration with Compose  
- **Coroutines & Flow** – For asynchronous operations and reactive data streams  

### **Database & Preferences**
- **Room** (`room-runtime`, `room-ktx`, `room-compiler`) – Local SQLite database  
- **DataStore** (`datastore-preferences`) – Persist user settings such as theme preferences  

### **Charting & Visualization**
- **Vico** (`com.patrykandsofia.vico:compose-m3`) – Modern charting library for the financial reports screen  

### **Build & Dependency Management**
- **Gradle + Version Catalogs** – Efficient dependency and build configuration  
- **KSP (Kotlin Symbol Processing)** – Used by Room for annotation-based code generation  

---

## 🚀 How to Build

1. **Clone** the repository  
   ```bash
   git clone https://github.com/<your-username>/rose-ledger.git
