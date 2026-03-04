# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
[Answer:
#### Important Considerations:
- What granularity do we need (SKU, order, warehouse)?
- Which cost drivers are realistic (labor hours, volume, weight, distance)?
- Whether to use Activity-Based Costing or simpler standard costing.
- Handling seasonal peaks and fluctuations.
- Aligning finance vs operations expectations.

#### Questions:
- How accurately do we track labor per activity?
- How do we account for damages, shrinkage, and returns?
- How should multi-stop transport be split across orders?
- What is the best way to allocate overhead—by space, hours, or throughput?
- Are all systems using the same cost centers and definitions? 
]

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
[Answer

#### Key Cost Optimization Strategies:
- **Labor efficiency:** better planning, cross‑training, small automation → lower labor hours per order.
- **Process improvements:** optimized picking, layout, fewer touches → faster operations.
- **Inventory optimization:** reduce overstock, balance stock → lower holding cost.
- **Transport optimization:** route optimization, carrier negotiation → lower shipping cost.
- **Facility & overhead reduction:** energy savings, better space use → lower fixed cost.
- **Technology upgrades:** better WMS/TMS and dashboards → fewer errors, higher visibility.

#### How to Identify, Prioritize & Implement:
- **Identify:** analyze cost drivers (labor, transport, storage), review KPIs, observe warehouse processes.
- **Prioritize:** use impact vs effort—start with quick wins that save money fast.
- **Implement:** run a pilot, measure results, standardize, then roll out to all sites.

#### Questions & Considerations:
- Where are the biggest costs today?
- What is the expected ROI for each improvement?
- Will any change affect service quality?
- Do we have accurate data to measure success?
- Which initiatives deliver the highest impact fastest?]

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
[ Answer
#### Importance:
- Real‑time, accurate cost data.
- No manual errors.
- Finance and operations fully aligned.

#### Benefits:
- Single source of truth.
- Faster reporting and budgeting.
- Automated cost allocation.

#### How to Ensure Integration:
- Use APIs for real‑time sync.
- Align cost centers/GL codes.
- Add validation, logging, and testing.

#### Questions & Considerations:
- Which financial system?
- What data needs real‑time sync?
- Who owns data quality?]

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
[ Answer
#### Importance:
- Predicts future fulfillment costs and resource needs.
- Supports better staffing, inventory planning, and financial control.
- Helps avoid overspending and enables proactive decision‑making.

#### What to Consider When Designing the System:
- Use historical cost data (labor, transport, inventory, overhead).
- Include seasonality, peaks, promotions, and growth trends.
- Integrate with financial + operational systems for real‑time data.
- Provide scenario modeling (best/worst/base case).
- Ensure accuracy with clear cost drivers and standardized data.

#### Questions & Considerations:
- What data sources feed the forecast?
- How often should forecasts update (monthly/weekly/real‑time)?
- What level of granularity is needed (SKU, warehouse, order)?
- How do we handle seasonality and sudden volume spikes?]

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
[ Answer
#### Cost Control Aspects:
- Keep old vs new warehouse costs clearly separated even with the same BU code.
- Preserve historical costs so baselines and trends remain accurate.
- Avoid losing data that helps set realistic budgets for the new warehouse.

#### Why Preserve Cost History:
- Enables comparison of old vs new performance.
- Provides a reliable cost baseline for staffing, capacity, and overhead.
- Helps detect unusual cost increases early.

#### How It Supports Budget Control:
- Historical data guides expected cost levels for the new site.
- Helps ensure the new warehouse operates within planned budget ranges.
- Supports accurate forecasting and KPI tracking.

#### Questions & Considerations:
- How do we archive the old warehouse’s costs?
- How do we distinguish new costs after going live?
- Do we need separate reporting views for old vs new?
- Which KPIs confirm the new warehouse is on budget?]

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
