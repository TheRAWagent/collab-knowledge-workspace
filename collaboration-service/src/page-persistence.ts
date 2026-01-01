import type { Document } from "@hocuspocus/server";
import {
  buildNormalizedSnapshot,
  validateSnapshot
} from "./pm-conversion-utils.ts";
import type { PageSnapshotV1 } from "./page-snapshot-v1.ts";
import { PageVersion } from "./page-version.ts";
import {
  type BlockSnapshotRequest,
  getOpenAPIDefinition
} from "./page-service.ts";

export async function persistDocument(documentId: string, document: Document) {


  const content = buildNormalizedSnapshot(document);

  const snapshot: PageSnapshotV1 = {
    schemaVersion: 1,
    pageId: documentId,
    generatedAt: new Date().toISOString(),
    updatedBy: Bun.randomUUIDv7(),
    source: "collaboration-service",
    content: content,
    version: PageVersion.getNextVersion(documentId)
  }

  const isSnapshotValid = validateSnapshot(snapshot);

  if (!isSnapshotValid) {
    console.error(`Invalid snapshot for page ${documentId}. Aborting save.`);
    return Promise.resolve();
  }

  getOpenAPIDefinition().saveSnapshot(documentId, snapshot as unknown as BlockSnapshotRequest).then(() => {
    console.log(`Snapshot for page ${documentId} saved successfully.`);
  }).catch(() => {
    console.error(`Failed to save snapshot for page ${documentId}.`);
  });


  console.log('SNAPSHOT (Hocuspocus debounced)', JSON.stringify(snapshot, null, 2));

  return Promise.resolve();
}