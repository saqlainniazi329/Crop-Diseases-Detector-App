const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ALICE_UID = "alice_crop_user";
const BOB_UID = "bob_crop_user";
const DATABASE_ID = "ai-studio-android-cropdoca-0d50fa60-6b0a-4667-9cc0-89cc455f5caf";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read any crop scans", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("scans").get());
});

test("Authenticated user cannot access other user's crop scans", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("scans").get());
});

test("Authenticated user can create their own crop scan record", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const scanDoc = aliceDb.collection("users").doc(ALICE_UID).collection("scans").doc("scan_123");
  
  await assertSucceeds(
    scanDoc.set({
      id: "scan_123",
      userId: ALICE_UID,
      cropType: "Rice",
      diseaseName: "Bacterial Blight",
      diseaseNameUrdu: "بیکٹیریل بلائٹ",
      confidence: 0.95,
      severity: "High",
      notes: "Detected during morning scouting",
      createdAt: new Date(),
    })
  );
});

test("Authenticated user cannot create scan with invalid disease name", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const scanDoc = aliceDb.collection("users").doc(ALICE_UID).collection("scans").doc("scan_invalid");
  
  await assertFails(
    scanDoc.set({
      id: "scan_invalid",
      userId: ALICE_UID,
      cropType: "Rice",
      diseaseName: "Alien Fungus", // Invalid enum
      diseaseNameUrdu: "نامعلوم",
      confidence: 0.95,
      severity: "High",
      createdAt: new Date(),
    })
  );
});

test("Authenticated user can list their own scans", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("scans").get()
  );
});
