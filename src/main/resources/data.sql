-- Managers
INSERT INTO managers (id, last_name, first_name)
VALUES (1, 'Martin', 'Claire'),
       (2, 'Durand', 'Paul');

-- Agencies
INSERT INTO agencies (id, code, created_at, manager_id)
VALUES (1, 'A0001', '2024-01-01', 1),
       (2, 'A0002', '2024-02-01', 2);

-- Advisors
INSERT INTO advisors (id, last_name, first_name, agency_id)
VALUES (1, 'Lefevre', 'Julie', 1),
       (2, 'Moreau', 'Thomas', 1),
       (3, 'Bernard', 'Sophie', 2);

-- Clients
INSERT INTO clients (
    id,
    last_name,
    first_name,
    address,
    postal_code,
    city,
    phone,
    type,
    advisor_id,
    agency_id
)
VALUES
    (1, 'Dupont', 'Jean', '1 rue de la Paix', '75001', 'Paris', '0600000001', 'PERSONAL', 1, 1),
    (2, 'Durand', 'Marie', '2 avenue Victor Hugo', '75016', 'Paris', '0600000002', 'PERSONAL', 1, 1),
    (3, 'AcmeCorp', 'SARL', '10 rue des Entreprises', '69001', 'Lyon', '0400000003', 'BUSINESS', 3, 2);

-- Accounts
INSERT INTO accounts (
    id,
    account_number,
    balance,
    opened_at,
    type,
    overdraft_limit,
    interest_rate,
    client_id
)
VALUES
    -- Client 1 : un compte courant + un compte épargne
    (1, 'CC-0001', 1500.00, '2024-03-01', 'CURRENT', 1000.00, NULL, 1),
    (2, 'CE-0001',  500.00, '2024-03-05', 'SAVINGS', NULL, 0.03, 1),

    -- Client 2 : un compte courant
    (3, 'CC-0002', -200.00, '2024-03-10', 'CURRENT', 1000.00, NULL, 2),

    -- Client 3 (entreprise) : un compte courant
    (4, 'CC-1001', -12000.00, '2024-01-15', 'CURRENT', 5000.00, NULL, 3);

-- Transfers
INSERT INTO transfers (
    amount,
    label,
    executed_at,
    from_account_id,
    to_account_id
)
VALUES
    (200.00, 'Virement loyer',        '2024-04-01T10:00:00', 1, 3),
    (150.00, 'Virement épargne',      '2024-04-05T09:30:00', 1, 2),
    (500.00, 'Règlement fournisseur', '2024-04-10T14:15:00', 4, 1);