# Named Elements Analyzer

![Build](https://github.com/stephan-james/named-elements/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/28215-named-elements.svg)](https://plugins.jetbrains.com/plugin/28215-named-elements)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28215-named-elements.svg)](https://plugins.jetbrains.com/plugin/28215-named-elements)

<!-- Plugin description -->
This is a powerful plugin designed to streamline the process of understanding and navigating your codebase. It scans a selected directory and generates a comprehensive, sortable table of all named code elements, tailored to the specific programming languages found within your project.

### Key Features:

- The plugin extracts and lists all significant named elements. This includes:
    - **Classes and Interfaces**
    - **Functions and Methods**
    - **Variables and Constants**
    - **Enums and Structs**
    - **Namespaces and Modules**
- **Interactive and Sortable Table:** The generated output is a clean, interactive table. You can easily **sort** the table columns by **element name**, **type**, or **count** to quickly find what you're looking for.
- **Quick Navigation:** Each entry in the table is an actionable link. Clicking on an element will instantly take you to its exact location in the source code, making it effortless to jump between definitions and implementations.

### How It Works:

1. **Select a Directory or the whole Projekt and start the action "**Analyze Named Elements...**".**
2. **Scan and Analyze:** The plugin will recursively scan the selected directory and analyze the files to extract all named elements.
3. **Generate Table:** A new panel or window will appear, displaying the interactive table with all the extracted information.

### Benefits:

- **Improved Code Comprehension:** Quickly get a high-level overview of a new or unfamiliar codebase.
- **Enhanced Productivity:** Spend less time searching for code and more time writing it.
- **Simplified Refactoring:** Easily identify all related functions, variables, or classes before making changes.

Whether you're a new developer joining a team or a senior engineer tasked with refactoring a legacy system, the **Named Elements Analyzer** is an indispensable tool for mastering your codebase.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "named-elements"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/28215-named-elements) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/28215-named-elements/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/stephan-james/named-elements/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
