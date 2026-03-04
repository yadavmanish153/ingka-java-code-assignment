# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
``` 
Yes, but only in an incremental, low risk way.

There are clearly multiple data access styles in use (e.g. hand written SQL / JDBC style, versus more abstracted repository patterns). That makes the code harder to read, onboard to, and evolve, because each feature may “speak a different dialect” to the database.

I would aim to converge on a single, consistent approach:

- Pick one primary abstraction (e.g. Spring Data–style repositories or a thin repository layer over JPA/ORM) and move all new work there.
- Wrap any lower level / JDBC style access behind repositories so services never see SQL, connections, or transaction details directly.
- Centralize transaction management and error handling instead of scattering `try/catch` and transaction boundaries across the code.
- Keep business logic out of repositories (use them only for persistence concerns) and move domain logic into services/entities.

Refactoring would be done incrementally: new features in the target style, and older code only migrated when it’s actually touched for new work or bug fixes. This improves maintainability, testability, and consistency without a risky “big bang” rewrite.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
OpenAPI (code‑generated) — Pros:
Consistent and standardized APIs
Automatic models/validation
Clear documentation and contracts
Easier long‑term maintenance

OpenAPI — Cons:
Requires maintaining the spec
Generated code can be rigid


Manual Coding — Pros:
Fast for small/simple endpoints
Full flexibility

Manual Coding — Cons:
Easy to create inconsistencies
No automatic documentation

My Choice:
More error‑prone and harder to scale
Use OpenAPI for all services. It keeps Product, Store, and Warehouse APIs aligned, easier to maintain, and more scalable as the platform grows.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```
To Prioritize Tests:
- Focus first on high‑risk areas (cost calculations, API integrations, financial sync).
- Next test high‑usage flows (common API calls, main user workflows).
- Then cover areas that change often (business rules, cost drivers).

Tests to Focus On:
- Unit tests: core logic and calculations.
- Integration/API tests: endpoints, data mapping, error handling.
- Small set of E2E tests: only for main business flows.
- Regression tests: automated, covering critical logic.

To Keep Coverage Effective Over Time:
- Automate key tests and run them on every PR.
- Use API contract testing to prevent breaking changes.
- Add tests for every discovered bug.
- Review and update the test suite regularly.
```