ALTER TABLE IF EXISTS supplies
    ADD CONSTRAINT supplies_materials_fk FOREIGN KEY (material_id) REFERENCES materials (id),
    ADD CONSTRAINT supplies_suppliers_fk FOREIGN KEY (supplier_id) REFERENCES suppliers (id);

ALTER TABLE IF EXISTS usage
    ADD CONSTRAINT usage_materials_fk FOREIGN KEY (material_id) REFERENCES materials (id),
    ADD CONSTRAINT usage_employees_fk FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE IF EXISTS supplier_materials
    ADD CONSTRAINT supplier_materials_supplier_fk FOREIGN KEY (supplier_id) REFERENCES suppliers (id) ON DELETE CASCADE,
    ADD CONSTRAINT supplier_materials_material_fk FOREIGN KEY (material_id) REFERENCES materials (id) ON DELETE CASCADE;