# Contributing to HealthPal

## Git Workflow

We follow a **feature branch workflow** with pull requests for code review.

### Branch Strategy

- **main**: Production-ready code
- **develop**: Integration branch for features
- **feature/**: Feature development branches
- **hotfix/**: Critical bug fixes

### Creating a Feature Branch

1. **Start from develop**
   ```bash
   git checkout develop
   git pull origin develop
   ```

2. **Create feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make changes and commit**
   ```bash
   git add .
   git commit -m "feat: add description of your feature"
   ```

### Commit Message Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `style:` Code style changes (formatting, etc.)
- `refactor:` Code refactoring
- `test:` Adding or updating tests
- `chore:` Maintenance tasks

Examples:
```
feat: add donation tracking endpoint
fix: resolve patient profile update issue
docs: update API documentation
```

### Pull Request Process

1. **Push your branch**
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Create Pull Request**
   - Go to GitHub/GitLab repository
   - Click "New Pull Request"
   - Select `develop` as base branch
   - Fill in PR template

3. **Code Review**
   - At least one team member must approve
   - Address review comments
   - Update PR as needed

4. **Merge**
   - Squash and merge into `develop`
   - Delete feature branch after merge

### Branch Protection Rules

- `main` branch:
  - Requires PR approval
  - Requires passing tests
  - No direct pushes

- `develop` branch:
  - Requires PR approval
  - No direct pushes (recommended)

## Code Style

### Java Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Keep methods under 50 lines
- Add JavaDoc comments for public methods
- Use Lombok annotations where appropriate

### Example

```java
/**
 * Creates a new treatment for a patient.
 *
 * @param treatment The treatment object to create
 * @return The created treatment with generated ID
 * @throws ResourceNotFoundException if patient not found
 */
@Transactional
public Treatment createTreatment(Treatment treatment) {
    // Implementation
}
```

## Testing Guidelines

### Unit Tests

- Write unit tests for all service methods
- Use mocking for dependencies
- Target 70%+ code coverage
- Test both success and error scenarios

### Integration Tests

- Test repository methods with `@DataJpaTest`
- Test controllers with `@WebMvcTest`
- Use test database (H2 in-memory)

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TreatmentServiceTest

# Run with coverage
mvn test jacoco:report
```

## Pull Request Template

When creating a PR, include:

1. **Description**
   - What changes were made
   - Why the changes were needed

2. **Testing**
   - How the changes were tested
   - Test results

3. **Checklist**
   - [ ] Code follows style guidelines
   - [ ] Tests added/updated
   - [ ] Documentation updated
   - [ ] No linter errors

## Code Review Checklist

Reviewers should check:

- [ ] Code follows project style guidelines
- [ ] Logic is correct and efficient
- [ ] Error handling is appropriate
- [ ] Tests are adequate
- [ ] Documentation is updated
- [ ] Security considerations addressed
- [ ] No hardcoded credentials or secrets
- [ ] API endpoints are properly secured

## Resolving Merge Conflicts

1. **Fetch latest changes**
   ```bash
   git fetch origin develop
   ```

2. **Rebase your branch**
   ```bash
   git rebase origin/develop
   ```

3. **Resolve conflicts**
   - Edit conflicted files
   - Remove conflict markers
   - Stage resolved files

4. **Continue rebase**
   ```bash
   git add .
   git rebase --continue
   ```

## Getting Help

- Check existing issues
- Ask team members
- Review code examples in the repository

## Reporting Issues

When reporting bugs:

1. Use the issue template
2. Include steps to reproduce
3. Include expected vs actual behavior
4. Add relevant logs/screenshots

## Development Setup

See [README.md](README.md) for setup instructions.

## Contact

For questions about contributing, contact:
- Omar Abumazen
- Saif Shayeb

