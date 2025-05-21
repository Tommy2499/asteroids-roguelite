# **Styles**

## General Guidelines
- All names should only include letters `a-z` and `A-Z`, numbers `0-9`, and underscore `_`.
- Maintain consistency across files.
- `google-java-format` is a useful VSCode extension that helps format Java and JavaScript code.

## Gitlab Guidelines

### Issue Labels
- **Priority 1**: The completion of the sprint is dependent upon the completion of this issue
- **Priority 2**: The sprint is possible to complete without this issue completed, however that is highly undesirable
- **Priority 3**: This issue is either not necessary to complete the current sprint, or is of low importance
- **Architecture**: This issue is related to the cross-functionality between the frontend, backend, and/or database. i.e. the communication that happens between them.

### Branches
- General branch name format: `[iss<ISSUENUMBER>/]<category>/<purpose>`. Ex: `iss23/doc/update_style`
- If no related issue number, ignore the first part with issue number. Ex: `bug/fix_mem_leak`
- For `<category>`, use easily recognizeable shorthands such as:
    - `feat`: add, remove, or modify feature
    - `hot`: hotfix (temporary solutions to critical problems. Document any need for permanent solution in merge)
    - `bug`: bugfix
    - `doc`: add, remove, or modify documentation
    - `wip`: work in progress (stuff that won't be finished soon)
    - `test`: for experimentation

### Commits
- General message format: `<Brief description of changes> in <affected location/functionalities(s)>`
- Use **past tense** (e.g., "Fixed bug in user authentication").
- Commit frequently: if you find yourself having trouble keeping the commit message concise, consider committing smaller chunks of work at a time.

### Merge Requests
- General title format: `<Brief high-level description of changes>. [Closes#ISSUENUMBER>]` 
- Use past tense just like for commit messages.
- If extra detail is required to describe changes, include them in the description (which needs to be written anyways)

## Code Formatting
### Indentation
Use **four** spaces to indent tabs. Many IDEs allow tab to be rebound to output 4 spaces in place of the tab `\t` character. 

### Braces & Spacing
- Use **K&R style** for curly braces:
    ```cpp
    if (condition) {
        doSomething();
    } else {
        doSomethingElse();
    }
    ```
- Always put a space before opening parentheses in control structures:
    ```python
    if (x == 5):
        print("x is 5")
    ```
- Always put a space before and after operation signs:
    ```python
    x = 10
    while (x <= 20):
    x += 1
    ```

### Comments
- As described in the Ousterhout book, write comments for a couple primary purposes:
    - **Interface comment**: Describes the high-level purpose of a class/method. Think: if I were oblivious to the implementation of this class/method, what would I need to know?
    - **Implementation comment**: Include *within* the implementation of a class/method to explain the reasoning behind unintuitive decisions, complicated solutions, or generally anything that is not easily decipherable from code itself.
    - **Data structure member**: Describes the function and/or purpose of a field declaration in a data structure
    - **Cross-module comment**: Describes the dependencies between modules
- Use single-line comments (`//` or `#`) for implementation comments and short data structure members. 
- Use multi-line comments (`/* ... */` or `''' ... '''`) for longer descriptions (multi-line, multi-paragraph, or to create visual aid such as expected console output or a diagram).
- Use multi-line comments (`/* ... */` or `''' ... '''`) for interface comments directly before classes/methods and follow any relevant documentation generator styles (Java JavaDOC, C++ Doxygen, Python Sphinx, etc.)
    ```java
    /**
     * This method checks whether a given score is greater than the currently stored high score
     * 
     * @param new_score: score to be compared with currently stored high score
     *
     * @return: true if the new score is higher than the currently stored high score, false otherwise
     */
    public boolean is_new_high_score(int new_score) {
        return new_score > high_score;
    }
    ```
    ```python
    '''
    @param x: int
    @return int
    '''
    def add_one(x):
        return x + 1
    ```

### Identifier Naming Conventions
- **Variables & Functions**:
    - **Java**: Use `camelCase`
    - **JS+React**: Use `camelCase`
    - **CSS**: Use `kebab-case`
    - **Constants**: Use `UPPER_CASE` (e.g., `MAX_RETRIES`).
- **Database Table Names**: Use `PascalCase`
- **Database Column Names**: Use `snake_case`
- **Classes**: Use `PascalCase` (e.g., `UserManager`).
- **Files**: Use `snake_case` (e.g., `data_processor.py`), unless another naming convention is needed or highly reccomended for a language (ex: `React`, `Markdown`, etc.).

## Language Specific Guidelines
*To be added onto those listed above if applicable.*

### CSS Guidelines
- Use **BEM naming convention** for class names:
    ```css
    .button {
        background: blue;
        color: white;
        padding: 10px 20px;
        border-radius: 5px;
    }

    .button--large {
        padding: 15px 30px;
    }

    .button__icon {
        margin-right: 5px;
    }
    ```

- Avoid `!important` unless absolutely necessary.
- Use variables for reusable values (if using SCSS):
    ```scss
    $primary-color: #3498db;
    ```

### JavaScript Guidelines
- Use **ES6+ syntax** (e.g., `const`, `let`, arrow functions).
- Prefer functional components over class components in React.
- Use `propTypes` or `TypeScript` for type safety in React.
- Use destructuring when handling function arguments:
  ```js
  function greet({ name, age }) {
      console.log(`Hello, ${name}. You are ${age} years old.`);
  }
  ```
- Use template literals instead of string concatenation:
  ```js
  const name = "John";
  console.log(`Hello, ${name}!`);
  ```
- Organize functions and modules logically, keeping related functions together.


### React Guidelines
- Use **JSX syntax** properly, wrapping multiple elements in a parent container:
  ```jsx
  function MyComponent() {
      return (
          <div>
              <h1>Hello, world!</h1>
          </div>
      );
  }
  ```
- Use **`useEffect`** and **`useState`** wisely to manage state and side effects.
- Use **context API or Redux** for global state management when necessary.
- Follow component composition best practices:
  ```jsx
  function Button({ text }) {
      return <button>{text}</button>;
  }
  
  function App() {
      return <Button text="Click Me" />;
  }
  ```
- Keep components **small and reusable**, breaking down large components into smaller ones.
- Use `TypeScript` for defining default values for props.

### Python Guidelines
- Follow **PEP 8** style guide.
- Use type hints where possible:
  ```python
  def add(x: int, y: int) -> int:
      return x + y
  ```
- Use list comprehensions instead of loops when appropriate.
