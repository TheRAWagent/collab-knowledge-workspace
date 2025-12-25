import { type JSONContent } from "@tiptap/react";
// import { BlockRequestType } from "../../api";
// import type { BlockRequest, BlockResponse } from "../../api";
//
export function blocksToTiptap(blocks): JSONContent {
  return {};
  // if (!blocks || blocks.length === 0) {
  //   return {
  //     type: "doc",
  //     content: [
  //       {
  //         type: "paragraph",
  //       },
  //     ],
  //   };
  // }
  //
  // const sortedBlocks = [...blocks].sort((a, b) => (a.orderIndex || 0) - (b.orderIndex || 0));
  //
  // const content: JSONContent[] = sortedBlocks.map((block) => {
  //   // Try to parse contentJson. If it has a "content" field, use it (rich text).
  //   // Otherwise fall back to "text" field (legacy/plain text).
  //   let blockContent: JSONContent[] | undefined;
  //   let textContent = "";
  //
  //   try {
  //     if (block.contentJson) {
  //       const parsed = JSON.parse(block.contentJson);
  //       if (parsed.content) {
  //         blockContent = parsed.content;
  //       } else if (parsed.text) {
  //         textContent = parsed.text;
  //       }
  //     }
  //   } catch (e) {
  //     // Ignore parse error
  //   }
  //
  //   // Helper to get content: prefer rich content, fallback to plain text
  //   const getContent = () => {
  //     if (blockContent) return blockContent;
  //     return textContent ? [{ type: "text", text: textContent }] : undefined;
  //   };
  //
  //   switch (block.type) {
  //     case BlockRequestType.HEADING_1:
  //       return {
  //         type: "heading",
  //         attrs: { level: 1 },
  //         content: getContent(),
  //       };
  //     case BlockRequestType.HEADING_2:
  //       return {
  //         type: "heading",
  //         attrs: { level: 2 },
  //         content: getContent(),
  //       };
  //     case BlockRequestType.HEADING_3:
  //       return {
  //         type: "heading",
  //         attrs: { level: 3 },
  //         content: getContent(),
  //       };
  //     case BlockRequestType.BULLET_LIST:
  //       return {
  //         type: "bulletList",
  //         content: [{
  //           type: "listItem",
  //           content: [{
  //             type: "paragraph",
  //             content: getContent()
  //           }]
  //         }]
  //       }
  //     case BlockRequestType.NUMBER_LIST:
  //       return {
  //         type: "orderedList",
  //         content: [{
  //           type: "listItem",
  //           content: [{
  //             type: "paragraph",
  //             content: getContent()
  //           }]
  //         }]
  //       }
  //     case BlockRequestType.TODO:
  //       // For TODO, we might need to extract checked state from contentJson if we stored it there
  //       let checked = false;
  //       try {
  //         if (block.contentJson) {
  //           const parsed = JSON.parse(block.contentJson);
  //           if (parsed.checked) checked = parsed.checked;
  //         }
  //       } catch (e) { }
  //
  //       return {
  //         type: "taskList",
  //         content: [{
  //           type: "taskItem",
  //           attrs: { checked },
  //           content: [{
  //             type: "paragraph",
  //             content: getContent()
  //           }]
  //         }]
  //       }
  //     case BlockRequestType.IMAGE:
  //       const imgData = block.contentJson ? JSON.parse(block.contentJson) : {};
  //       return {
  //         type: "image",
  //         attrs: {
  //           src: imgData.src,
  //           alt: imgData.alt,
  //           title: imgData.title
  //         }
  //       }
  //     case BlockRequestType.CODE:
  //       return {
  //         type: "codeBlock",
  //         content: getContent()
  //       }
  //     case BlockRequestType.DIVIDER:
  //       return { type: "horizontalRule" };
  //     case BlockRequestType.BLOCKQUOTE:
  //       return { type: "blockquote" };
  //     case BlockRequestType.PARAGRAPH:
  //     default:
  //       return {
  //         type: "paragraph",
  //         content: getContent(),
  //       };
  //   }
  // });
  //
  // return {
  //   type: "doc",
  //   content,
  // };
}

export function tiptapToBlocks(content: JSONContent, parentId: string) {
  // if (!content.content) {
  //   return [];
  // }
  // console.log(content)
  // const blocks: BlockRequest[] = [];
  //
  // content.content.forEach((node, index) => {
  //   let type: BlockRequestType = BlockRequestType.PARAGRAPH;
  //   let contentData: any = {};
  //
  //   // Helper to extract content from a node (e.g. paragraph inside list item)
  //   // We want to store the array of text/marks
  //   const extractContent = (n: JSONContent) => {
  //     return n.content || [];
  //   };
  //
  //   if (node.type === "heading") {
  //     if (node.attrs?.level === 1) type = BlockRequestType.HEADING_1;
  //     else if (node.attrs?.level === 2) type = BlockRequestType.HEADING_2;
  //     else if (node.attrs?.level === 3) type = BlockRequestType.HEADING_3;
  //     contentData = { content: extractContent(node) };
  //
  //   } else if (node.type === "bulletList") {
  //     if (node.content) {
  //       node.content.forEach((listItem, liIndex) => {
  //         // listItem -> paragraph -> content
  //         const innerPara = listItem.content?.[0];
  //         const innerContent = innerPara ? extractContent(innerPara) : [];
  //
  //         blocks.push({
  //           parentId,
  //           type: BlockRequestType.BULLET_LIST,
  //           contentJson: JSON.stringify({ content: innerContent }),
  //           orderIndex: index * 100 + liIndex
  //         });
  //       });
  //       return;
  //     }
  //   } else if (node.type === "orderedList") {
  //     if (node.content) {
  //       node.content.forEach((listItem, liIndex) => {
  //         const innerPara = listItem.content?.[0];
  //         const innerContent = innerPara ? extractContent(innerPara) : [];
  //
  //         blocks.push({
  //           parentId,
  //           type: BlockRequestType.NUMBER_LIST,
  //           contentJson: JSON.stringify({ content: innerContent }),
  //           orderIndex: index * 100 + liIndex
  //         });
  //       });
  //       return;
  //     }
  //   } else if (node.type === "taskList") {
  //     if (node.content) {
  //       node.content.forEach((taskItem, tiIndex) => {
  //         const innerPara = taskItem.content?.[0];
  //         const innerContent = innerPara ? extractContent(innerPara) : [];
  //         const checked = taskItem.attrs?.checked || false;
  //
  //         blocks.push({
  //           parentId,
  //           type: BlockRequestType.TODO,
  //           contentJson: JSON.stringify({ content: innerContent, checked }),
  //           orderIndex: index * 100 + tiIndex
  //         });
  //       });
  //       return;
  //     }
  //   } else if (node.type === "image") {
  //     type = BlockRequestType.IMAGE;
  //     contentData = {
  //       src: node.attrs?.src,
  //       alt: node.attrs?.alt,
  //       title: node.attrs?.title
  //     };
  //   } else if (node.type === "codeBlock") {
  //     type = BlockRequestType.CODE;
  //     contentData = { content: extractContent(node) };
  //   } else if (node.type === "horizontalRule") {
  //     type = BlockRequestType.DIVIDER;
  //   } else if(node.type === "blockquote") {
  //     type = BlockRequestType.BLOCKQUOTE;
  //     contentData = { content: extractContent(node) };
  //   } else {
  //     // Paragraph and others
  //     contentData = { content: extractContent(node) };
  //   }
  //
  //   blocks.push({
  //     parentId,
  //     type,
  //     contentJson: JSON.stringify(contentData),
  //     orderIndex: index * 100,
  //   });
  // });
  // console.log(blocks)
  //
  // return blocks;
}
