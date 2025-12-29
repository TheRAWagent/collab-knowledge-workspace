import { Document } from "@hocuspocus/server";
import { randomUUIDv7 } from "bun";
import { isBlockNode } from "./utils.ts";
import type { ProseMirrorDoc, ProseMirrorNode } from "./ProseMirrorDoc.ts";
import type { PageSnapshotV1 } from "./page-snapshot-v1.ts";
import { yXmlFragmentToProsemirrorJSON } from "y-prosemirror";

const addIds = (doc: ProseMirrorDoc) => {
  const addIdsHelper = (json: ProseMirrorNode) => {

    if (!isBlockNode(json.type)) {
      json.content?.forEach((child: ProseMirrorNode) => addIdsHelper(child));
      return json;
    }
    if (!json.attrs) {
      json.attrs = {};
    }
    if (!json.attrs.blockId) {
      json.attrs.blockId = randomUUIDv7();
      json.content?.forEach((child: ProseMirrorNode) => addIdsHelper(child));
    }
    return json;
  }
  doc.content.forEach((child: ProseMirrorNode) => addIdsHelper(child));
  return doc;
}

export const buildNormalizedSnapshot = (snapshot: Document): ProseMirrorDoc => {
  const xml = snapshot.getXmlFragment("prosemirror");
  const json = yXmlFragmentToProsemirrorJSON(xml) as unknown as ProseMirrorDoc;
  return addIds(json);
}

export const validateSnapshot = (snapshot: PageSnapshotV1): boolean => {
  const { content } = snapshot;
  // Basic validation to ensure that all block nodes have blockId
  const validateNode = (node: ProseMirrorNode): boolean => {
    if (isBlockNode(node.type)) {
      if (!node.attrs || !node.attrs.blockId) {
        return false;
      }
    }
    if (node.content) {
      for (const child of node.content) {
        if (!validateNode(child)) {
          return false;
        }
      }
    }
    return true;
  }

  //Check if block ids are unique
  const blockIds = new Set<string>();
  const collectBlockIds = (node: ProseMirrorNode) => {
    if (isBlockNode(node.type) && node.attrs && node.attrs.blockId) {
      if (blockIds.has(node.attrs.blockId)) {
        throw new Error(`Duplicate blockId found: ${node.attrs.blockId}`);
      }
      blockIds.add(node.attrs.blockId);
    }
    if (node.content) {
      for (const child of node.content) {
        collectBlockIds(child);
      }
    }
  }

  try {
    collectBlockIds(content);
  } catch (e) {
    return false;
  }

  return validateNode(content);
}
