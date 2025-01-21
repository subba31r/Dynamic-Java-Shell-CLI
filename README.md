# ⚡ Dynamic Java Shell: A Custom CLI ⚙️

A lightweight, customizable Java-based shell that emulates a command-line interface. This program supports various commands like `echo`, `cd`, `type`, and `cat`, enabling users to interact with files and directories in an intuitive way. 🚀

---

## 📝 Features

- 🖋️ **Echo Command**: Print and manipulate strings with special character handling.
- 📂 **Change Directory (`cd`)**: Navigate through directories easily.
- 📜 **Cat Command**: Read and display file contents, with support for multiple files.
- 🛠️ **Type Command**: Check if a command is built-in or an executable.
- 📌 **Path Handling**: Execute commands or navigate directories using relative and absolute paths.
- 🛑 **Exit Command**: Cleanly exit the shell with a status code.

---

## 🛠️ Usage

### 📚 Commands Overview

| Command                | Description                                       | Example                      |
|------------------------|---------------------------------------------------|------------------------------|
| `echo <text>`          | Print text or manipulate strings.                | `echo "Hello, World!"`       |
| `cd <path>`            | Change the current working directory.            | `cd /home/user/documents`    |
| `cat <file1> <file2>`  | Display contents of files.                       | `cat file1.txt file2.txt`    |
| `type <command>`       | Check if a command is built-in or executable.    | `type echo`                  |
| `exit <code>`          | Exit the shell with a status code.               | `exit 0`                     |
| `pwd`                  | Print the current working directory.             | `pwd`                        |

## 🔗 Examples

```bash
$ echo "Java CLI"
Java CLI

$ pwd
/home/user/documents

$ type echo
echo is a shell builtin

$ cd /path/to/directory
/path/to/directory

$ echo "Java" > file1.txt

$ echo "Shell" >file2.txt

$ cat file1.txt file2.txt
Java Shell

$ exit 0

